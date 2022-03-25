INSERT INTO public."scheduled_task" (id, "taskName", "nextDateTime")
VALUES (gen_random_uuid(), 'REMOVE_DEACTIVATED_RECORDS', '2020-06-25 00:00:00');

INSERT INTO public."scheduled_task" (id, "taskName", "nextDateTime")
VALUES (gen_random_uuid(), 'DEACTIVATE_UNVERIFIED_ACCOUNTS', now());