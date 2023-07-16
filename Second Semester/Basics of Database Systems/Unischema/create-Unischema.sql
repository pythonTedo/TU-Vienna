CREATE TABLE Studenten (
	MatrNr integer PRIMARY KEY,
	Name varchar(30) NOT NULL,
	Sem smallint NOT NULL
);

CREATE TABLE Professoren (
	PersNr integer PRIMARY KEY,
	Name varchar(30) NOT NULL,
	Rang char(2) NOT NULL,
	Raum integer NOT NULL
);

CREATE TABLE Vorlesungen (
	VorlNr integer primary key,
	Titel varchar(20) NOT NULL,
	SWS smallint NOT NULL,
	gelesenVon integer NOT NULL REFERENCES Professoren
);

CREATE TABLE voraussetzen (
	VorgNr integer NOT NULL REFERENCES Vorlesungen(VorlNr),
	NachfNr integer NOT NULL REFERENCES Vorlesungen(VorlNr),
	PRIMARY KEY (VorgNr, NachfNr)
);

CREATE TABLE hoeren (
	MatrNr integer NOT NULL REFERENCES Studenten,
	VorlNr integer NOT NULL REFERENCES Vorlesungen,
	PRIMARY KEY (MatrNr, VorlNr)
);

CREATE TABLE Assistenten (
	PersNr integer PRIMARY KEY,
	Name varchar(30) NOT NULL,
	Fachgebiet varchar(30) NOT NULL,
	Boss integer NOT NULL REFERENCES Professoren(PersNr)
);

CREATE TABLE pruefen (
	MatrNr integer NOT NULL REFERENCES Studenten(MatrNr),
	VorlNr integer NOT NULL REFERENCES Vorlesungen(VorlNr),
	PersNr integer NOT NULL REFERENCES Professoren(PersNr),
	Note smallint,
	PRIMARY KEY (MatrNr, VorlNr, PersNr)
);