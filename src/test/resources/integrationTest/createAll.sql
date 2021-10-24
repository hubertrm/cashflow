INSERT INTO ACCOUNT (name, creation_date) VALUES ('name', TO_DATE('01/01/21','dd/MM/yy'));
INSERT INTO CATEGORY (name, creation_date) VALUES ('name', TO_DATE('01/01/21','dd/MM/yy'));
INSERT INTO TRANSACTION (date, amount, category_id, account_id, description)
VALUES (TO_DATE('31/12/21','dd/MM/yy'), 1, (
    SELECT id FROM CATEGORY WHERE name='name'
), (
    SELECT id FROM ACCOUNT WHERE name='name'
), 'description');