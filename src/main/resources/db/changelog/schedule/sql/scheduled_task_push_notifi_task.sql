INSERT INTO public."scheduled_task" (id, task_name, next_date_time)
VALUES (gen_random_uuid(), 'SEND_PUSH_NOTIFICATIONS', now());