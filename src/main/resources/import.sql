insert into address (id, street, number, district, city, zip_code, state) values (1, 'Av. Barao do Rio Branco', '2200', 'Centro', 'Juiz de Fora', '36010-010', 'MG');
insert into address (id, street, number, district, city, zip_code, state) values (2, 'Av. Independencia', '700', 'Sao Mateus', 'Juiz de Fora', '36010-020', 'MG');

insert into customer (id, name, email, register_number, type, phone_number, address_id) values (1, 'Fulano', 'fulano@email.com', '111.111.111-11', 'LEGAL_PERSON', '1234-5678', 1);
insert into customer (id, name, email, register_number, type, phone_number, address_id) values (2, 'Ciclano', 'ciclano@email.com', '222.222.222-22', 'LEGAL_PERSON', '2345-6789', 1);
