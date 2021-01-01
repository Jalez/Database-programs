-- työ(työ_tyyppi(PK), tuntihinta, alv) --
INSERT INTO työ VALUES('suunnittelu', 55,24);
INSERT INTO työ VALUES('asennustyö', 45, 24);
INSERT INTO työ VALUES('aputyö', 35, 24);
-- asiakas(asiakas_id(PK), asiakas_nimi, asiakas_osoite) --
INSERT INTO asiakas VALUES(121, 'Matti Meikäläinen', 'Hämeenkatu 6');
INSERT INTO asiakas VALUES(221, 'John Doe', 'Itsenäisyydenkatu 12');
INSERT INTO asiakas VALUES(321, 'Maija Meemiläinen', 'Yliopistonkatu 20');
-- työkohde(kohde_id(PK), kohteen_osoite, asiakas_id, vähennyskelpoisuus) --
INSERT INTO työkohde VALUES(001, 'Valiuksenkatu 5', 121, 'E');
INSERT INTO työkohde VALUES(002, 'Kaislakuja 6', 221, 'E');
INSERT INTO työkohde VALUES(003, 'Kaislakuja 7', 221, 'E');
INSERT INTO työkohde VALUES(004, 'Hepokuja 22', 321, 'K');
-- sopimus(sopimus_id(PK), kohde_id, sopimus_tyyppi, s_erät)
INSERT INTO sopimus VALUES(100, 001, 'urakka', 1); 
INSERT INTO sopimus VALUES(101, 002, 'tuntityo', 1);
INSERT INTO sopimus VALUES(102, 003, 'tuntityo', 3);
-- lasku(lasku_id(PK), sopimus_id, laskun_tyyppi, laskun_tila, laskutuspvm, eräpvm, laskutuskerta, laskutus_erä, maksupvm, maksun_tila, laskutuslisä, viivästyskorko)
INSERT INTO lasku VALUES(00100, 100, 'lasku', 'valmis', '04.01.2020','04.12.2020', 1, 1,NULL ,'maksamatta', 0, 0);
INSERT INTO lasku VALUES(00121, 101, 'lasku', 'valmis', '04.03.2020','04.14.2020', 1, 1,NULL ,'maksamatta', 0, 0);
INSERT INTO lasku VALUES(02101, 102, 'lasku', 'valmis', '04.05.2020','04.17.2020', 1, 1,'04.15.2020','maksettu', 0, 0);
INSERT INTO lasku VALUES(02102, 102, 'lasku', 'valmis', '04.17.2020','04.30.2020', 1, 2,'04.26.2020' ,'maksettu', 0, 0);
INSERT INTO lasku VALUES(02103, 102, 'lasku', 'valmis', '05.01.2020','05.12.2020', 1, 3,NULL ,'maksamatta', 0, 0);
-- työsuoritus(sopimus_id(PK), työ_tyyppi(PK), tunnit, alennus)
INSERT INTO työsuoritus VALUES(100, 'suunnittelu', 15, 10);
INSERT INTO työsuoritus VALUES(100, 'asennustyö', 20, 15);
INSERT INTO työsuoritus VALUES(101, 'suunnittelu', 10, 30);
INSERT INTO työsuoritus VALUES(101, 'asennustyö', 30, 0);
INSERT INTO työsuoritus VALUES(102, 'suunnittelu', 3, 20);
INSERT INTO työsuoritus VALUES(102, 'asennustyö', 12, 10);
-- tarvike(t_id(PK), t_nimi, yksikkö, varastotilanne, sisäänostohinta, myyntihinta, alv)
INSERT INTO tarvike VALUES(99, 'pistorasia', 'kpl', 50, 5, 10, 24);
INSERT INTO tarvike VALUES(98, 'naulakiinnike', 'kpl', 1000, 1, 2, 24);
INSERT INTO tarvike VALUES(60, 'sähköjohto', 'metri', 400, 1, 2, 24);
INSERT INTO tarvike VALUES(20, 'nippusidepussi', 'kpl', 155, 2, 4, 24);
INSERT INTO tarvike VALUES(30, 'välikytkin, valkoinen', 'kpl', 220, 10, 20, 24);
INSERT INTO tarvike VALUES(31, 'mekaaninen termostaatti', 'kpl', 100, 40, 75, 24);
INSERT INTO tarvike VALUES(32, 'älytermostaatti', 'kpl', 20, 100, 150, 24);
--tarvikeluettelo (sopimus_id(PK), tarvike_id(PK), lkm, alennus) 
INSERT INTO tarvikeluettelo VALUES(100, 99, 10, 30);
INSERT INTO tarvikeluettelo VALUES(100, 98, 2, 30);
INSERT INTO tarvikeluettelo VALUES(100, 60, 3, 0);
INSERT INTO tarvikeluettelo VALUES(101, 99, 4, 30);
INSERT INTO tarvikeluettelo VALUES(102, 99, 1, 0);
INSERT INTO tarvikeluettelo VALUES(102, 60, 3, 0);
INSERT INTO tarvikeluettelo VALUES(100, 31, 1, 0);
