--- Query 1 -- Number of expensive orders per exchange (i.e. price > 1M euros)
CREATE OR REPLACE VIEW NumberOfExpensiveOrders AS
	SELECT Boerse, count(*)
	FROM Orders
	WHERE Preis > 1000000
	GROUP BY Boerse;

--- Query 2 -- Transitive closure of holds
CREATE OR REPLACE VIEW AllHolds AS
WITH RECURSIVE tmp(FWID, FDName, FBName, WWID, WDName, WBName) as ( 
       SELECT FWID, FDName, FBName, WWID, WDName, WBName 
	   FROM Gewichtung
       UNION
       SELECT tmp.FWID, tmp.FDName, tmp.FBName, g.WWID, g.WDName, g.WBName 
	   FROM Gewichtung g, tmp 
	   WHERE tmp.WWID = g.FWID AND tmp.WDName = g.FDName AND tmp.WBName = g.FBName
       ) SELECT * FROM tmp;
SELECT * FROM AllHolds;


--- Query 3 -- Distance and commulative product of weights of allholds
CREATE OR REPLACE VIEW WeightingOfHolds AS
WITH RECURSIVE tmp(FWID, FDName, FBName, WWID, WDName, WBName, comWeight, distance) as ( 
       SELECT FWID, FDName, FBName, WWID, WDName, WBName, Gewicht, 1 
	   FROM Gewichtung
       UNION
       SELECT tmp.FWID, tmp.FDName, tmp.FBName, g.WWID, g.WDName, g.WBName, (tmp.comWeight * g.Gewicht)::NUMERIC(10,2), tmp.distance + 1
	   FROM Gewichtung g, tmp 
	   WHERE tmp.WWID = g.FWID AND tmp.WDName = g.FDName AND tmp.WBName = g.FBName
       ) SELECT * FROM tmp;
SELECT * FROM WeightingOfHolds;
