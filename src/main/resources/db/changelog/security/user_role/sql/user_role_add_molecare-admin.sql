INSERT INTO user_role ("user_id", "role_id")
SELECT id, '6f098a0d-5745-400b-ae9f-d3c58b76ba0b'
FROM account a
WHERE a.email = 'trisavvys@gmail.com'
