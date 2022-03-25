update public."account"
set email_account_id = ea.id
from ( select id, email from public."email_account") as ea
where public."account".email = ea.email;