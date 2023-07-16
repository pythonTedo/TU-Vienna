DROP FUNCTION CreateSecurities(INTEGER, VARCHAR, NUMERIC);

DROP TRIGGER trCalculateKEST ON Verkauforder;
DROP FUNCTION fCalculateKEST();

DROP TRIGGER trFeeAdjustment ON Boerse;
DROP FUNCTION fFeeAdjustment();

DROP TRIGGER trTFCUpdate ON fonds;
DROP FUNCTION fTFCUpdate();


DROP VIEW NumberOfExpensiveOrders;
DROP VIEW AllHolds;
DROP VIEW WeightingOfHolds;

ALTER TABLE Boerse DROP CONSTRAINT fk_ExchangeStock;
ALTER TABLE Broker DROP CONSTRAINT fk_BrokerDepot;

DROP TABLE Verkauforder;
DROP TABLE Kauforder;
DROP TABLE Orders;
DROP TABLE unterstuetzt;
DROP TABLE Boerse;
DROP TABLE Gewichtung;
DROP TABLE Anleihen;
DROP TABLE Aktien;
DROP TABLE Fonds;
DROP TABLE Dividende;
DROP TABLE Wertpapier;
DROP TABLE Depot;
DROP TABLE Broker;
DROP TYPE BrokerType;

DROP SEQUENCE seq_weighting;
DROP SEQUENCE seq_security;