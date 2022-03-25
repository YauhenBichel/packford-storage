INSERT INTO user_role ("user_id", "role_id")
SELECT id, '2cc4015f-bd0e-4a2f-8339-57f6119d10bf'
FROM account a
WHERE a.email = 'trisavvys@gmail.com'
