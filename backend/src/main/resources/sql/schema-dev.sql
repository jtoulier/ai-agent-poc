CREATE SCHEMA IF NOT EXISTS credits;
CREATE TABLE IF NOT EXISTS credits.relationshipManagers(
    relationshipManagerId   VARCHAR(16) NOT NULL PRIMARY KEY,
    relationshipManagerName VARCHAR(64) NOT NULL,
    password                VARCHAR(256) NOT NULL,
    threadId                VARCHAR(36),
    writtenAt               TIMESTAMP NOT NULL
);
