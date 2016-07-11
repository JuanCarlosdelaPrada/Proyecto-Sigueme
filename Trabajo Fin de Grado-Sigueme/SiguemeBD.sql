--usuario_id es el correo electrónico
CREATE TABLE USUARIO (
	usuario_id VARCHAR(64),
	contrasena BLOB NOT NULL,
	rol BOOLEAN NOT NULL DEFAULT 0,
	nombre VARCHAR(32) NOT NULL,
	apellidos VARCHAR(64) NOT NULL,
	dni VARCHAR(9) NOT NULL UNIQUE,
	direccion VARCHAR(45) NOT NULL,
	fecha_nacimiento DATE NOT NULL,
	telefono VARCHAR(9) NOT NULL,
	sexo VARCHAR(6) NOT NULL,	
	club VARCHAR(64),
	federado BOOLEAN NOT NULL DEFAULT 0,
	PRIMARY KEY(usuario_id)
);

CREATE TABLE RUTA (
	ruta_id VARCHAR(75),
	descripcion VARCHAR(360),
	dificultad VARCHAR(15) NOT NULL,
	distancia DECIMAL(12,8) NOT NULL,
	fichero_gpx VARCHAR(120) NOT NULL,
	lat_min DECIMAL(10,8) NOT NULL,
	lat_max DECIMAL(10,8) NOT NULL,
	long_min DECIMAL(11,8) NOT NULL,
	long_max DECIMAL(11,8) NOT NULL,
	PRIMARY KEY (ruta_id)
);

CREATE TABLE PRUEBA (
	prueba_id VARCHAR(75),
	ruta_id VARCHAR(75) NOT NULL,
	descripcion VARCHAR(360),
	lugar VARCHAR(90) NOT NULL, 
	fecha_cel DATE NOT NULL,
	hora_cel TIME NOT NULL,
	fecha_inscrip_min DATE NOT NULL,
	fecha_inscrip_max DATE NOT NULL,
	maximo_inscritos INTEGER NOT NULL,
	activa BOOLEAN NOT NULL DEFAULT 0,
	PRIMARY KEY (prueba_id),
	FOREIGN KEY (ruta_id) REFERENCES RUTA(ruta_id)
);

CREATE TABLE INSCRITO (
	prueba_id VARCHAR(75),
	usuario_id VARCHAR(64),
	pagado BOOLEAN NOT NULL DEFAULT 0,
	dorsal INT NOT NULL,
	PRIMARY KEY (prueba_id, usuario_id),
	FOREIGN KEY (prueba_id) REFERENCES PRUEBA(prueba_id),
	FOREIGN KEY (usuario_id) REFERENCES USUARIO(usuario_id)
);

CREATE TABLE POSICION (
	prueba_id VARCHAR(75),
	usuario_id VARCHAR(64),
	fecha DATE,
	hora TIME,
	latitud DECIMAL(10,8) NOT NULL,
	longitud DECIMAL(11,8) NOT NULL,
	PRIMARY KEY (prueba_id, usuario_id, fecha, hora),
	FOREIGN KEY (prueba_id) REFERENCES PRUEBA(prueba_id),
	FOREIGN KEY (usuario_id) REFERENCES USUARIO(usuario_id)
);

CREATE TABLE IVBYTES (
	ivbytes_id BLOB,
	usuario_id VARCHAR(64),
	PRIMARY KEY(usuario_id),
	FOREIGN KEY (usuario_id) REFERENCES USUARIO(usuario_id)
);