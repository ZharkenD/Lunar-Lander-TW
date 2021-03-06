CREATE TABLE USERS (
    ID SERIAL PRIMARY KEY,
    NAME_REAL VARCHAR(50),
    USERNAME VARCHAR(30),
    PASSWORD VARCHAR(60),
    EMAIL VARCHAR(50)
);

CREATE TABLE CONFIGURATIONS (
    ID SERIAL PRIMARY KEY,
    USER_ID INTEGER REFERENCES USERS,
    CONFIGURE_NAME VARCHAR(30),
    DIFF_ID INTEGER,
    SPACESHIP_ID INTEGER,
    PLANET_ID INTEGER
);

CREATE TABLE SCORES (
    ID SERIAL PRIMARY KEY,
    CONF_ID INTEGER REFERENCES CONFIGURATIONS (ID),
    SPEED REAL,
    FUEL REAL,
    INIT_TIME TIMESTAMP,
    END_TIME TIMESTAMP
);

