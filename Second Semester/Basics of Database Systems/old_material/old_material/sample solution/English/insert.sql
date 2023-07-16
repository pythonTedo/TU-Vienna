BEGIN;

/* The order of the DELETE statements is important */

DELETE FROM sell;
DELETE FROM buy;
DELETE FROM orders;
DELETE FROM supports;
DELETE FROM exchange;
DELETE FROM weighting;
DELETE FROM bond;
DELETE FROM stock;
DELETE FROM fund;
DELETE FROM dividend;
DELETE FROM security;
DELETE FROM depot;
DELETE FROM broker;


SELECT setval('seq_weighting', 5000, false);
SELECT setval('seq_security', 0, false);

COMMIT;


BEGIN;

INSERT INTO broker(BName, KEY, URL, BrokerDepot, brokerageFee) VALUES
		('Interactive Brokers', 'KEY1', 'URL1', 'Broker Depot of I.B.', 10),
		('Fidelity', 'KEY2', 'URL2', 'Fidelity Broker Depot', 15),
		('Zacks Trade', 'KEY2', 'URL3', 'Zacks Broker Depot', 20);
		

INSERT INTO depot(DName, costs, BName) VALUES
		('Broker Depot of I.B.', 10.20 , 'Interactive Brokers'),
		('Fidelity Broker Depot', 30.50 , 'Fidelity'),
		('Zacks Broker Depot', 25.99 , 'Zacks Trade');

COMMIT;

SELECT CreateSecurities(10, 'TU Depot', 20);

INSERT INTO weighting(weight, FSID, FDName, FBName, WSID, WDName, WBName) VALUES
		(0.5, 0, 'TU Depot', 'Interactive Brokers', 6, 'TU Depot', 'Interactive Brokers'),
		(0.5, 0, 'TU Depot', 'Interactive Brokers', 10, 'TU Depot', 'Interactive Brokers'),
		(0.5, 6, 'TU Depot', 'Interactive Brokers', 12, 'TU Depot', 'Interactive Brokers'),
		(0.5, 12, 'TU Depot', 'Interactive Brokers', 18, 'TU Depot', 'Interactive Brokers'),
		(0.5, 18, 'TU Depot', 'Interactive Brokers', 2, 'TU Depot', 'Interactive Brokers'),
		(0.5, 18, 'TU Depot', 'Interactive Brokers', 4, 'TU Depot', 'Interactive Brokers');

INSERT INTO exchange(name, Tz, exStockSID, exStockDepot, exStockBroker, exchangeFee) VALUES
		('American Exchange', 'A', 2, 'TU Depot', 'Interactive Brokers', 10),
		('European Exchange', 'A', 8, 'TU Depot', 'Interactive Brokers', 50),
		('Asian Exchange', 'A', 14, 'TU Depot', 'Interactive Brokers', 20);
		
INSERT INTO supports(BName, exchange) VALUES
		('Interactive Brokers', 'American Exchange'),
		('Fidelity', 'American Exchange'),
		('Zacks Trade', 'American Exchange'),
		('Interactive Brokers', 'European Exchange'),
		('Zacks Trade', 'European Exchange'),
		('Fidelity', 'Asian Exchange'),
		('Zacks Trade', 'Asian Exchange');

INSERT INTO orders(time, OId, fee, price, nr, exchange, SID, DName, BName) VALUES
		('2010-01-05', 0, 10, 150, 0, 'American Exchange', 14, 'TU Depot', 'Interactive Brokers'),
		('2020-11-10', 1, 10, 2000, 0, 'American Exchange', 2, 'TU Depot', 'Interactive Brokers'),
		('2021-02-02', 2, 10, 2000000, 0, 'American Exchange', 4, 'TU Depot', 'Interactive Brokers'),
		('2021-05-04', 3, 10, 2500000, 1, 'American Exchange', 14, 'TU Depot', 'Interactive Brokers'),
		('2021-08-03', 4, 10, 800, 2, 'American Exchange', 2, 'TU Depot', 'Interactive Brokers'),
		('2021-04-02', 5, 10, 1200000, 3, 'American Exchange', 12, 'TU Depot', 'Interactive Brokers'),
		('2021-04-02', 6, 10, 1000, 3, 'American Exchange', 10, 'TU Depot', 'Interactive Brokers');

INSERT INTO buy(orderID) VALUES
		(0),
		(1),
		(2);

INSERT INTO sell(orderID, KEST) VALUES
		(3, 0),
		(4, 0),
		(5, 0),
		(6, 0);

INSERT INTO dividend(date, price, yield, SID, DName, BName) VALUES
		('2010-01-05', 100, 20, 2, 'TU Depot', 'Interactive Brokers'),
		('2020-06-11', 100, 40, 4, 'TU Depot', 'Interactive Brokers'),
		('2021-09-12', 100, 10, 6, 'TU Depot', 'Interactive Brokers');