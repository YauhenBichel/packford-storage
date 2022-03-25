INSERT INTO public."email_account" (id, email, password, verificated, created, modified)
SELECT gen_random_uuid(), email, password, verificated, created, modified FROM public."account";