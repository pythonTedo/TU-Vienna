CREATE SEQUENCE seq_security INCREMENT BY 2 MINVALUE 0 NO CYCLE;;
CREATE SEQUENCE seq_weighting INCREMENT BY 12 MINVALUE 5000 NO CYCLE;
CREATE TYPE BrokerType AS ENUM ('Interactive Brokers', 'Fidelity', 'Zacks Trade');


CREATE TABLE broker (
	BName BrokerType NOT NULL,
	KEY VARCHAR(255) NOT NULL,
	URL VARCHAR(255) NOT NULL,
	brokerDepot VARCHAR(255) NOT NULL,
	brokerageFee NUMERIC(10, 2),
	PRIMARY KEY(BName)
);

CREATE TABLE depot (
	DName VARCHAR(255) NOT NULL,
	costs NUMERIC(10,2) NOT NULL,
	BName BrokerType NOT NULL,
	FOREIGN KEY(BName) REFERENCES broker(BName),
	PRIMARY KEY(BName, DName)
);

CREATE TABLE security (
	SID INTEGER NOT NULL DEFAULT NEXTVAL('seq_security'),
	Isin VARCHAR(12) NOT NULL CHECK(Isin ~* $$^([a-z]){2}\w{10}$$),
	name VARCHAR(255) NOT NULL,
	DName VARCHAR(255) NOT NULL,
	BName BrokerType NOT NULL,
	FOREIGN KEY(BName, DName) REFERENCES depot(BName, DName),
	PRIMARY KEY(BName, DName, SID)
);

CREATE TABLE dividend (
	date DATE NOT NULL CHECK(date > '1940-01-01'::date),
	price NUMERIC(10,2) NOT NULL,
	yield NUMERIC(10,2) NOT NULL,
	SID INTEGER NOT NULL,
	DName VARCHAR(255) NOT NULL,
	BName BrokerType NOT NULL,
	FOREIGN KEY(BName, DName, SID) REFERENCES security(BName, DName, SID),
	PRIMARY KEY(date, BName, DName, SID)
);

CREATE TABLE fund (
	TFC NUMERIC(10,2) NOT NULL,
	SID INTEGER NOT NULL,
	DName VARCHAR(255) NOT NULL,
	BName BrokerType NOT NULL,
	FOREIGN KEY(BName, DName, SID) REFERENCES security(BName, DName, SID),
	PRIMARY KEY(BName, DName, SID)
);

CREATE TABLE stock (
	SID INTEGER NOT NULL,
	DName VARCHAR(255) NOT NULL,
	BName BrokerType NOT NULL,
	FOREIGN KEY(BName, DName, SID) REFERENCES security(BName, DName, SID),
	PRIMARY KEY(BName, DName, SID)
);


CREATE TABLE bond (
	SID INTEGER NOT NULL,
	DName VARCHAR(255) NOT NULL,
	BName BrokerType NOT NULL,
	FOREIGN KEY(BName, DName, SID) REFERENCES security(BName, DName, SID),
	PRIMARY KEY(BName, DName, SID)
);


CREATE TABLE weighting (
	GID INTEGER DEFAULT NEXTVAL('seq_weighting'),
	weight NUMERIC(10, 2) NOT NULL CHECK(weight > 0 AND weight <= 1),
	FSID INTEGER NOT NULL,
	FDName VARCHAR(255) NOT NULL,
	FBName BrokerType NOT NULL,
	WSID INTEGER NOT NULL,
	WDName VARCHAR(255) NOT NULL,
	WBName BrokerType NOT NULL,
	UNIQUE(FBName, FDName, FSID, WBName, WDName, WSID),
	FOREIGN KEY(FBName, FDName, FSID) REFERENCES fund(BName, DName, SID),
	FOREIGN KEY(WBName, WDName, WSID) REFERENCES security(BName, DName, SID),
	PRIMARY KEY(GID)
);

CREATE TABLE exchange (
	name VARCHAR(255) NOT NULL,
	Tz VARCHAR(255) NOT NULL,
	exStockBroker BrokerType NOT NULL,
	exStockDepot VARCHAR(255) NOT NULL,
	exStockSID INTEGER NOT NULL,
	exchangeFee NUMERIC(10, 2),
	PRIMARY KEY(name)
);

ALTER TABLE broker ADD CONSTRAINT fk_BrokerDepot
      FOREIGN KEY (BName, brokerDepot) REFERENCES depot(BName, DName)
      DEFERRABLE INITIALLY DEFERRED;


ALTER TABLE exchange ADD CONSTRAINT fk_ExchangeStock
      FOREIGN KEY (exStockBroker, exStockDepot, exStockSID) REFERENCES stock(BName, DName, SID)
      DEFERRABLE INITIALLY DEFERRED;


CREATE TABLE supports (
	BName BrokerType NOT NULL,
	exchange VARCHAR(255) NOT NULL,
	FOREIGN KEY(BName) REFERENCES broker(BName),
	FOREIGN KEY(exchange) REFERENCES exchange(name),
	PRIMARY KEY(BName, exchange)
);

CREATE TABLE orders (
	time DATE NOT NULL,
	OId INTEGER NOT NULL,
	fee NUMERIC(10,2) NOT NULL,
	price NUMERIC(10,2) NOT NULL,
	nr INTEGER NOT NULL,
	exchange VARCHAR(255) NOT NULL,
	SID INTEGER NOT NULL,
	DName VARCHAR(255) NOT NULL,
	BName BrokerType NOT NULL,
	FOREIGN KEY(exchange) REFERENCES exchange(name),
	FOREIGN KEY(BName, DName, SID) REFERENCES security(BName, DName, SID),
	PRIMARY KEY(OId)
);

CREATE TABLE buy (
	orderID INTEGER NOT NULL,
	FOREIGN KEY(orderID) REFERENCES orders(OId),
	PRIMARY KEY(orderID)
);

CREATE TABLE sell (
	orderID INTEGER NOT NULL,
	KEST NUMERIC(10, 2) NOT NULL,
	FOREIGN KEY(orderID) REFERENCES orders(OId),
	PRIMARY KEY(orderID)
);
