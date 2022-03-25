package com.ybichel.storage.common.search;

import com.google.common.collect.Sets;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EntitySearchUtil {
    private static final Set<Class> PLAIN_MAPPED_TYPES = Sets.newHashSet(Number.class, CharSequence.class, Temporal.class, UUID.class);
    private static final Predicate<Map.Entry<String, Boolean>> SEARCHABLE_FILTER = Map.Entry::getValue;
    private static final Predicate<Map.Entry<String, Boolean>> IGNORED_FILTER = SEARCHABLE_FILTER.negate();

    private EntitySearchUtil() {
        //hidden
    }

    public static Set<String> getSearchableFieldNames(Class clazz) {
        return getFields(clazz).entrySet().stream()
                .filter(SEARCHABLE_FILTER)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public static Set<String> getIgnoredFieldNames(Class clazz) {
        return getFields(clazz).entrySet().stream()
                .filter(IGNORED_FILTER)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public static boolean isSearchable(Field field) {
        if (Modifier.isStatic(field.getModifiers()) ||
                !isColumn(field) && !isEmbeddedOrJoined(field) && !isFormula(field)) {
            return false;
        }
        final Searchable searchable = field.getAnnotation(Searchable.class);
        return Objects.nonNull(searchable) && searchable.value() || Objects.isNull(searchable);
    }

    private static String appendName(@Nullable String baseName, String fieldName) {
        if (baseName == null) {
            return fieldName;
        }
        return baseName + '.' + fieldName;
    }

    /**
     * If field is a Collection returns the generic Type used in it. Otherwise return the type of the field.
     */
    private static Class getFieldType(Field field) {
        Class fieldType = field.getType();
        if (Collection.class.isAssignableFrom(fieldType)) {
            return getGenericTypeOfCollection(field);
        } else {
            return fieldType;
        }
    }

    /**
     * @param field Must be a Collection
     */
    private static Class getGenericTypeOfCollection(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType)//redundant check?
        {
            ParameterizedType pt = (ParameterizedType) genericType;
            final Type[] typeArguments = pt.getActualTypeArguments();
            if (typeArguments.length != 1) {
                //should never happen. Probably possible if Map is passed as param, but Maps are not supported yet.
                throw new RuntimeException("Generic type of " + field.getName() + " has more than 1 actual type arguments.");
            }
            Type type = typeArguments[0];
            if (type instanceof Class) //checking to throw ClassCastException with custom message.
            {
                return (Class) type;
            } else {
                //should never happen actually
                throw new ClassCastException("Actual type of " + type.getTypeName() + " type of " + field.getName() + " field is not instanceof Class");
            }
        } else {
            //should never happen
            throw new RuntimeException("Generic type of " + field.getName() + " field is not instanceof ParameterizedType");
        }
    }

    private static boolean isColumn(Field field) {
        return Objects.nonNull(field.getAnnotation(Column.class)) ||
                Objects.nonNull(field.getAnnotation(org.hibernate.annotations.Type.class));
    }

    private static boolean isFormula(Field field) {
        return Objects.nonNull(field.getAnnotation(org.hibernate.annotations.Formula.class));
    }

    private static boolean isEmbeddedOrJoined(Field field) {
        return Objects.nonNull(field.getAnnotation(Embedded.class)) ||
                Objects.nonNull(field.getAnnotation(OneToOne.class)) ||
                Objects.nonNull(field.getAnnotation(ManyToOne.class)) ||
                Objects.nonNull(field.getAnnotation(OneToMany.class)) ||
                Objects.nonNull(field.getAnnotation(ManyToMany.class)) ||
                Objects.nonNull(field.getAnnotation(EmbeddedId.class));
    }

    public static Map<String, Boolean> getFields(Class clazz) {
        List<Class> hierarchy = getClassHierarchy(clazz);

        final Map<String, Boolean> fields = new HashMap<>();

        hierarchy.forEach(c -> {
            Map<String, Boolean> superFields = getFields(Arrays.asList(c.getDeclaredFields()), null, hierarchy, false);
            fields.putAll(superFields);
        });

        return fields;
    }

    private static List<Class> getClassHierarchy(Class clazz) {
        List<Class> hierarchy = new ArrayList<>();
        hierarchy.add(clazz);

        Class superclass = clazz.getSuperclass();

        while (superclass != null && !superclass.equals(Object.class)) {
            hierarchy.add(superclass);
            superclass = superclass.getSuperclass();
        }
        Collections.reverse(hierarchy);
        return hierarchy;
    }

    private static Map<String, Boolean> getFields(List<Field> fields, @Nullable String baseName, Collection<Class> parentClasses, boolean ignoreAll) {
        Map<String, Boolean> fieldNames = new HashMap<>();
        for (Field f : fields) {
            Class fieldType = getFieldType(f);
            if (parentClasses.contains(fieldType)) {
                continue;
            }
            Set<Class> updatedParentClasses = new HashSet<>(parentClasses);
            updatedParentClasses.add(fieldType);
            if (isSearchable(f)) {
                fieldNames.putAll(getSearchableFieldAndInnersNames(f, baseName, updatedParentClasses, ignoreAll));
            } else {
                fieldNames.putAll(getIgnoredFieldAndInnersNames(f, baseName, updatedParentClasses));
            }
        }
        return fieldNames;
    }

    private static Map<String, Boolean> getSearchableFieldAndInnersNames(Field field, @Nullable String baseName, Collection<Class> parentClasses, boolean ignoreAll) {
        String name = getName(field, baseName);

        if (isColumn(field) || isFormula(field)) {
            Map<String, Boolean> fieldNames = new HashMap<>();
            fieldNames.put(name, !ignoreAll);
            return fieldNames;
        }
        Class fieldType = getFieldType(field);

        List<Class> hierarchy = getClassHierarchy(fieldType);

        final Map<String, Boolean> fields = new HashMap<>();

        hierarchy.forEach(c -> {
            Map<String, Boolean> superFields = getFields(Arrays.asList(c.getDeclaredFields()), name, parentClasses, ignoreAll);
            fields.putAll(superFields);
        });

        return fields;
    }

    private static Map<String, Boolean> getIgnoredFieldAndInnersNames(Field field, @Nullable String baseName, Collection<Class> parentClasses) {
        String name = getName(field, baseName);

        if (isColumn(field) || isPlainMapped(field.getType())) {
            Map<String, Boolean> fieldNames = new HashMap<>();
            fieldNames.put(name, false);
            return fieldNames;
        }
        Class fieldType = getFieldType(field);

        List<Class> hierarchy = getClassHierarchy(fieldType);

        final Map<String, Boolean> fields = new HashMap<>();

        hierarchy.forEach(c -> {
            Map<String, Boolean> superFields = getFields(Arrays.asList(c.getDeclaredFields()), name, parentClasses, true);
            fields.putAll(superFields);
        });

        return fields;
    }

    private static String getName(Field field, @Nullable String baseName) {
        if (baseName == null) {
            return field.getName();
        } else {
            return appendName(baseName, field.getName());
        }
    }

    private static boolean isPlainMapped(Class clazz) {
        return clazz.isEnum() || clazz.isPrimitive() || PLAIN_MAPPED_TYPES.stream().anyMatch(c -> c.isAssignableFrom(clazz));
    }
}
