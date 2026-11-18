-- Gestor de Torneos NBA - Esquema MySQL
-- Persisten Equipo y Jugador. Partidos y entrenador quedan en memoria.
-- Se puede ejecutar las veces que haga falta: recrea las tablas vacias.
CREATE DATABASE IF NOT EXISTS tp_nba;
USE tp_nba;

DROP TABLE IF EXISTS jugadores;
DROP TABLE IF EXISTS equipos;

CREATE TABLE equipos (
  id          INT PRIMARY KEY,
  nombre      VARCHAR(50) NOT NULL UNIQUE,
  ciudad      VARCHAR(50) NOT NULL,
  conferencia ENUM('ESTE','OESTE') NOT NULL
);

-- equipo_id puede ser NULL: un jugador existe por si solo (agregacion)
-- y si se borra su equipo queda sin equipo en lugar de borrarse.
CREATE TABLE jugadores (
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
