DROP FUNCTION CreateSecurities(INTEGER, VARCHAR, NUMERIC);

DROP TRIGGER trCalculateKEST ON sell;
DROP FUNCTION fCalculateKEST();

DROP TRIGGER trFeeAdjustment ON exchange;
DROP FUNCTION fFeeAdjustment();

DROP TRIGGER trTFCUpdate ON fund;
DROP FUNCTION fTFCUpdate();


DROP VIEW NumberOfExpensiveOrders;
DROP VIEW AllHolds;
DROP VIEW WeightingOfHolds;

ALTER TABLE exchange DROP CONSTRAINT fk_ExchangeStock;
ALTER TABLE broker DROP CONSTRAINT fk_BrokerDepot;

DROP TABLE sell;
DROP TABLE buy;
DROP TABLE orders;
DROP TABLE supports;
DROP TABLE exchange;
DROP TABLE weighting;
DROP TABLE bond;
DROP TABLE stock;
DROP TABLE fund;
DROP TABLE dividend;
DROP TABLE security;
DROP TABLE depot;
DROP TABLE broker;
DROP TYPE BrokerType;

DROP SEQUENCE seq_weighting;
DROP SEQUENCE seq_security;