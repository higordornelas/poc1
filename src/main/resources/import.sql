insert into address (id, street, number, district, city, zip_code, state, is_main) values (1, 'Av. Barao do Rio Branco', '2200', 'Centro', 'Juiz de Fora', '36010-010', 'MG', true);
insert into address (id, street, number, district, city, zip_code, state, is_main) values (2, 'Av. Independencia', '700', 'Sao Mateus', 'Juiz de Fora', '36010-020', 'MG', true);
insert into address (id, street, number, district, city, zip_code, state, is_main) values (3, 'Av. Rebouças', '120', 'Centro', 'Sao Paulo', '24110-010', 'SP', false);
insert into address (id, street, number, district, city, zip_code, state, is_main) values (4, 'Rua Joal de Lima e Silva', '321', 'Vila Júlia', 'Uruguaiana', '97500-350', 'RS', false);
insert into address (id, street, number, district, city, zip_code, state, is_main) values (5, 'Rua Monte Caseros', '42', 'Vila Sílvia Regina', 'Campo Grande', '79103-170', 'MS', false);

insert into customer (id, name, email, register_number, type, phone_number) values (1, 'Fulano', 'fulano@email.com', '111.111.111-11', 'LEGAL_PERSON', '1234-5678');
insert into customer (id, name, email, register_number, type, phone_number) values (2, 'Ciclano', 'ciclano@email.com', '222.222.222-22', 'LEGAL_PERSON', '2345-6789');

insert into customer_addresses(customer_id, addresses_id) values(1, 1);
insert into customer_addresses(customer_id, addresses_id) values(2, 2);