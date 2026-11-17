-- Gestor de Torneos NBA - Esquema MySQL
-- Persisten Equipo y Jugador. Partidos, usuarios y tabla de posiciones
-- quedan en memoria (la tabla se recalcula desde los partidos).
CREATE DATABASE IF NOT EXISTS tp_nba;
USE tp_nba;

CREATE TABLE IF NOT EXISTS equipos (
                                       id          INT PRIMARY KEY,
                                       nombre      VARCHAR(50) NOT NULL UNIQUE,
    ciudad      VARCHAR(50) NOT NULL,
    conferencia ENUM('ESTE','OESTE') NOT NULL
    );

-- equipo_id puede ser NULL: un jugador existe por si solo (agregacion)
-- y si se borra su equipo queda sin equipo en lugar de borrarse.
CREATE TABLE IF NOT EXISTS jugadores (
                                         id               INT PRIMARY KEY,
                                         equipo_id        INT NULL,
                                         nombre           VARCHAR(50) NOT NULL,
    apellido         VARCHAR(50) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    nacionalidad     VARCHAR(40),
    dorsal           TINYINT NOT NULL,
    posicion         ENUM('BASE','ESCOLTA','ALERO','ALA_PIVOT','PIVOT') NOT NULL,
    altura           DECIMAL(3,2) NOT NULL,
    FOREIGN KEY (equipo_id) REFERENCES equipos(id) ON DELETE SET NULL,
    UNIQUE (equipo_id, dorsal)
    );
