DROP TABLE työ CASCADE;
DROP TABLE asiakas CASCADE;
DROP TABLE työkohde CASCADE;
DROP TABLE sopimus CASCADE;
DROP TABLE lasku CASCADE;
DROP TABLE työsuoritus CASCADE;
DROP TABLE tarvike CASCADE;
DROP TABLE tarvikeluettelo CASCADE;
CREATE TABLE työ(
työ_tyyppi VARCHAR(20) PRIMARY KEY, -- Tunti suunnittelu/rakennus
tuntihinta INT NOT NULL,
alv INT NOT NULL
);
CREATE TABLE asiakas(
asiakas_id INT PRIMARY KEY,
asiakas_nimi VARCHAR(50) NOT NULL,
asiakas_osoite VARCHAR(50) NOT NULL
); 
CREATE TABLE työkohde(
kohde_id INT PRIMARY KEY,
kohteen_osoite VARCHAR(50) NOT NULL,
asiakas_id INT NOT NULL,
vähennyskelpoisuus VARCHAR(1) NOT NULL, -- Onko kohde kotitalousvähennyskelpoinen: K=kyllä/E=ei.
FOREIGN KEY(asiakas_id) REFERENCES asiakas
);
CREATE TABLE sopimus(
sopimus_id INT PRIMARY KEY, -- Yksittäisen sopimuksen tunnus.
kohde_id INT NOT NULL, -- Sopimuksen solmineen kohteen tunnus (sopimuksella on aina oltava kohde)
sopimus_tyyppi VARCHAR(10) NOT NULL, -- urakka/tuntityö
sopimus_erät INT NOT NULL CHECK (sopimus_erät > 0), -- Erien lukumäärä 
FOREIGN KEY(kohde_id) REFERENCES työkohde
);
CREATE TABLE lasku(
lasku_id INT PRIMARY KEY,-- PK: Yksittäisen laskun tunnus: 1-999 999
sopimus_id INT NOT NULL,
lasku_tyyppi VARCHAR(20) NOT NULL, -- Laskun tyyppi: lasku/muistutuslasku/karhulasku
laskun_tila VARCHAR(10) NOT NULL, -- Laskun tila: kesken/valmis
laskutuspvm DATE, -- Päivämäärä, jolloin lasku lähetetty: MM.DD.YYYY
eräpvm DATE, -- Eräpäivä, johon mennessä lasku pitäisi olla maksettu: MM.DD.YYYY
laskutuskerta INT NOT NULL CHECK (laskutuskerta > 0), -- Kuinka mones laskutuskerta kyseisestä erästä: 1->
laskun_erä INT NOT NULL CHECK (laskun_erä > 0), -- Mikä laskun eristä kyseessä: aina oltava vähintään 1.
maksupvm DATE, -- Maksupäivä, jolloin lasku on maksettu: null - MM.DD.YYYY
maksun_tila VARCHAR(10) DEFAULT 'maksamatta', -- Maksun tila: maksamatta/maksettu
laskutuslisä INT, -- muistutuslaskuun lisätään laskutuslisä
viivästyskorko INT, -- karhulaskuun liittyy laskutuslisän lisäksi myös viivästyskorko
FOREIGN KEY(sopimus_id) REFERENCES sopimus
);
CREATE TABLE työsuoritus( -- Kyseessä ER-kaaviossa heikko entiteettityyppi
sopimus_id INT NOT NULL, 
työ_tyyppi VARCHAR NOT NULL, -- Työn tyypit: "suunnittelu", "työ", "aputyö"
tunnit INT NOT NULL, -- Tehdyt tunnit on suoritettu työsuorituskirjausta tehdessä.
alennus INT, -- Työsuoritukseen liittyvästä alennuksesta. 
PRIMARY KEY(sopimus_id, työ_tyyppi),
FOREIGN KEY(työ_tyyppi) REFERENCES työ,
FOREIGN KEY(sopimus_id) REFERENCES sopimus
);
CREATE TABLE tarvike(
tarvike_id INT PRIMARY KEY,
tarvike_nimi VARCHAR(50) NOT NULL,
yksikkö VARCHAR(10) NOT NULL CHECK(yksikkö<> 'metri' OR yksikkö <> 'kpl'), -- Työkalujen kohdalla: Onko kyseessä kpl(esim pistorasiat)/metri(esim: sähköjohto)
varastotilanne INT NOT NULL, -- Työkalu: varaston tilanne
sisäänostohinta INT NOT NULL, -- Työkalu: pitää antaa sisäänostohinta
myyntihinta INT NOT NULL, -- Työkalu: pitää antaa myyntihinta.
alv INT NOT NULL -- Työkalu: Pitää antaa alv  
);
CREATE TABLE tarvikeluettelo(
sopimus_id INT NOT NULL,
tarvike_id INT NOT NULL,
lkm INT CHECK(lkm > 0), -- Tunti/kappalemäärä/metri, riippuen myyntiartikkelin tyypistä
alennus INT, --Jos alennuksia sisältyy hintaan. Prosenttiosuus (0/30/50/70)
PRIMARY KEY(sopimus_id, tarvike_id),
FOREIGN KEY(sopimus_id) REFERENCES sopimus,
FOREIGN KEY(tarvike_id) REFERENCES tarvike
);