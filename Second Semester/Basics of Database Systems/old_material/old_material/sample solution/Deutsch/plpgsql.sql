--- Trigger for 4.a --- 
CREATE OR REPLACE FUNCTION fCalculateKEST() RETURNS TRIGGER AS $$
DECLARE
    KEST NUMERIC(10,2);
	p NUMERIC(10, 2);
BEGIN
	SELECT Preis INTO p FROM Orders WHERE OID = NEW.OrderId;
	
	KEST := p * 0.275;

	IF NEW.KEST = 0 OR NEW.KEST = KEST THEN 
		NEW.KEST := KEST;
		RETURN NEW;
	END IF;
	
	RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trCalculateKEST BEFORE INSERT
    ON Verkauforder FOR EACH ROW EXECUTE PROCEDURE fCalculateKEST();
	
--- Trigger for 4.b ---
CREATE OR REPLACE FUNCTION fFeeAdjustment() RETURNS TRIGGER AS $$
DECLARE
	rec RECORD;
BEGIN
	--- Update any broker who supports the updated exchange ---
	IF NEW.BoersenGeb != OLD.BoersenGeb THEN
		FOR rec in (SELECT b.BName, b.BrokerageGeb FROM Broker b, unterstuetzt u WHERE b.BName = u.BName AND u.Boerse = NEW.Name) LOOP
			UPDATE Broker SET BrokerageGeb = BrokerageGeb + (NEW.BoersenGeb - OLD.BoersenGeb) WHERE BName = rec.BName;
			RAISE NOTICE 'Brokerage fee was set to %euros.', (rec.BrokerageGeb + (NEW.BoersenGeb - OLD.BoersenGeb));
		END LOOP;
	ELSIF NEW.BoersenGeb = OLD.BoersenGeb THEN
		RAISE WARNING 'Exchange fee did not change!';        
	END IF;					    

    RETURN NEW;
	END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trFeeAdjustment BEFORE UPDATE OF BoersenGeb ON Boerse
       FOR EACH ROW EXECUTE PROCEDURE fFeeAdjustment();


--- Trigger for 4.c ---
CREATE OR REPLACE FUNCTION fTFCUpdate() RETURNS TRIGGER AS $$
BEGIN
	IF NEW.TFC != OLD.TFC THEN
		UPDATE Fonds SET TFC = TFC + (NEW.TFC - OLD.TFC)
       	    WHERE (WID, DName, BName) in (SELECT g.FWID, g.FDName, g.FBName FROM Gewichtung g 
				WHERE OLD.WID = g.WWID AND OLD.DName = g.WDName AND OLD.BName = g.WBName);
	END IF;
		RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trTFCUpdate AFTER UPDATE OF TFC ON Fonds
       FOR EACH ROW EXECUTE PROCEDURE fTFCUpdate();


--- Prozedure for 4.d ---
CREATE OR REPLACE FUNCTION CreateSecurities(IN numSecurities INTEGER, IN DepotName VARCHAR, IN Kosten NUMERIC) RETURNS VOID AS
$$
DECLARE
    BrokerName BrokerTYPE;
	someIsin VARCHAR(12);
BEGIN

    IF numSecurities < 1 THEN
        RAISE EXCEPTION 'Number of securities must at least be 1';
    END IF;

	IF EXISTS(SELECT * FROM Depot d WHERE d.DName = DepotName) THEN
        RAISE EXCEPTION 'Depot name "%" already exists!', DepotName;
    END IF;
	
	    -- Create depot
	SELECT BName INTO BrokerName FROM Broker b LIMIT 1;	
	INSERT INTO Depot VALUES (DepotName, Kosten, BrokerName);
	
		-- Create securities
	FOR counter IN 0 .. numSecurities - 1
    LOOP
		someIsin = 'ATnnnnnnnnC' || counter;
        CASE counter % 3 
			WHEN 0 THEN			
				INSERT INTO Wertpapier(WID, Isin, Name, DName, BName) VALUES 
				(NEXTVAL('seq_security'), someIsin, concat('Fund of ', DepotName), DepotName, BrokerName);
				INSERT INTO Fonds VALUES 
				(counter * 10, CURRVAL('seq_security'), DepotName, BrokerName);
            WHEN 1 THEN
				INSERT INTO Wertpapier(WID, Isin, Name, DName, BName) VALUES 
				(NEXTVAL('seq_security'), someIsin, concat('Stock of ', DepotName), DepotName, BrokerName);
				INSERT INTO Aktien VALUES 
				(CURRVAL('seq_security'), DepotName, BrokerName);
            WHEN 2 THEN
				INSERT INTO Wertpapier(WID, Isin, Name, DName, BName) VALUES 
				(NEXTVAL('seq_security'), someIsin, concat('Bond of ', DepotName), DepotName, BrokerName);
				INSERT INTO Anleihen VALUES 
				(CURRVAL('seq_security'), DepotName, BrokerName);
        END CASE;
   END LOOP;
END;
$$ LANGUAGE plpgsql;