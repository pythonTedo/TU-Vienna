--- Trigger for 4.a --- 
CREATE OR REPLACE FUNCTION fCalculateKEST() RETURNS TRIGGER AS $$
DECLARE
    KEST NUMERIC(10,2);
	p NUMERIC(10, 2);
BEGIN
	SELECT price INTO p FROM orders WHERE OID = NEW.orderId;
	
	KEST := p * 0.275;

	IF NEW.KEST = 0 OR NEW.KEST = KEST THEN 
		NEW.KEST := KEST;
		RETURN NEW;
	END IF;
	
	RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trCalculateKEST BEFORE INSERT
    ON sell FOR EACH ROW EXECUTE PROCEDURE fCalculateKEST();
	
--- Trigger for 4.b ---
CREATE OR REPLACE FUNCTION fFeeAdjustment() RETURNS TRIGGER AS $$
DECLARE
	rec RECORD;
BEGIN
	--- Update any broker who supports the updated exchange ---
	IF NEW.exchangeFee != OLD.exchangeFee THEN
		FOR rec in (SELECT b.BName, b.brokerageFee FROM broker b, supports s WHERE b.BName = s.BName AND s.exchange = NEW.name) LOOP
			UPDATE broker SET brokerageFee = brokerageFee + (NEW.exchangeFee - OLD.exchangeFee) WHERE BName = rec.BName;
			RAISE NOTICE 'Brokerage fee was set to %euros.', (rec.brokerageFee + (NEW.exchangeFee - OLD.exchangeFee));
		END LOOP;
	ELSIF NEW.exchangeFee = OLD.exchangeFee THEN
		RAISE WARNING 'Exchange fee did not change!';        
	END IF;					    

    RETURN NEW;
	END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trFeeAdjustment BEFORE UPDATE OF exchangeFee ON exchange
       FOR EACH ROW EXECUTE PROCEDURE fFeeAdjustment();


--- Trigger for 4.c ---
CREATE OR REPLACE FUNCTION fTFCUpdate() RETURNS TRIGGER AS $$
BEGIN
	IF NEW.TFC != OLD.TFC THEN
		UPDATE fund SET TFC = TFC + (NEW.TFC - OLD.TFC)
       	    WHERE (SID, DName, BName) in (SELECT w.FSID, w.FDName, w.FBName FROM weighting w 
				WHERE OLD.SID = w.WSID AND OLD.DName = w.WDName AND OLD.BName = w.WBName);
	END IF;
		RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trTFCUpdate AFTER UPDATE OF TFC ON fund
       FOR EACH ROW EXECUTE PROCEDURE fTFCUpdate();


--- Prozedure for 4.d ---
CREATE OR REPLACE FUNCTION CreateSecurities(IN numSecurities INTEGER, IN depotName VARCHAR, IN costs NUMERIC) RETURNS VOID AS
$$
DECLARE
    brokerName BrokerTYPE;
	someIsin VARCHAR(12);
BEGIN

    IF numSecurities < 1 THEN
        RAISE EXCEPTION 'Number of securities must at least be 1';
    END IF;

	IF EXISTS(SELECT * FROM depot d WHERE d.DName = depotName) THEN
        RAISE EXCEPTION 'Depot name "%" already exists!', depotName;
    END IF;
	
	    -- Create depot
	SELECT BName INTO brokerName FROM broker b LIMIT 1;	
	INSERT INTO depot VALUES (depotName, costs, brokerName);
	
		-- Create Securities
			
	FOR counter IN 0 .. numSecurities - 1
    LOOP
		someIsin = 'ATnnnnnnnnC' || counter;
        CASE counter % 3 
			WHEN 0 THEN			
				INSERT INTO security(SID, Isin, name, DName, BName) VALUES 
				(NEXTVAL('seq_security'), someIsin, concat('Fund of ', depotName), depotName, brokerName);
				INSERT INTO fund VALUES 
				(counter * 10, CURRVAL('seq_security'), depotName, brokerName);
            WHEN 1 THEN
				INSERT INTO security(SID, Isin, name, DName, BName) VALUES 
				(NEXTVAL('seq_security'), someIsin, concat('Stock of ', depotName), depotName, brokerName);
				INSERT INTO stock VALUES 
				(CURRVAL('seq_security'), depotName, brokerName);
            WHEN 2 THEN
				INSERT INTO security(SID, Isin, name, DName, BName) VALUES 
				(NEXTVAL('seq_security'), someIsin, concat('Bond of ', depotName), depotName, brokerName);
				INSERT INTO bond VALUES 
				(CURRVAL('seq_security'), depotName, brokerName);
        END CASE;
   END LOOP;
END;
$$ LANGUAGE plpgsql;