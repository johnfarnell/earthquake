INSERT INTO STATION (ID, SRC) values (1, 'NZ');
INSERT INTO STATION (ID, SRC) values (2, 'US');
INSERT INTO REGION (ID, NAME) values (1, 'Nicobar Islands, India region');
INSERT INTO REGION (ID, NAME) values (2, 'South Sandwich Islands region');

INSERT INTO EARTHQUAKE(ID, STATION_ID, EQ_ID, TIME_DATE, LATITUDE, LONGITUDE, MAGNITUDE, DEPTH, REGION_ID) values (1, 1, 'EQ-ID1', {ts '2012-09-17 18:47:52.69'}, 60, 88, 3.66, 25.789, 1);
INSERT INTO EARTHQUAKE(ID, STATION_ID, EQ_ID, TIME_DATE, LATITUDE, LONGITUDE, MAGNITUDE, DEPTH, REGION_ID) values (2, 2, 'EQ-ID2', {ts '2016-11-15 23:25:16.32'}, 50, -76, 7.45, 87.65, 2);

INSERT INTO USERS (USER_ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME) values(1, 'admin', 'password', 'Tommy','Admin');
INSERT INTO USERS (USER_ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME) values(2, 'stranger', 'password', 'Tommy','Stranger');