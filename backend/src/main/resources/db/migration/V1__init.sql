CREATE sequence hibernate_sequence;

CREATE TABLE USERS(
    user_id bigint NOT NULL,
    email character varying(255)  NOT NULL,
    first_name character varying(255) ,
    last_name character varying(255) ,
    password character varying(255) ,
    username character varying(255)  NOT NULL,
    uuid character varying(255) ,
    CONSTRAINT users_pkey PRIMARY KEY (user_id),
    CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
    CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username)
);

CREATE TABLE AUTH_TOKENS(
    id bigint NOT NULL,
    last_used timestamp without time zone NOT NULL,
    token character varying(255)  NOT NULL,
    username character varying(255)  NOT NULL,
    CONSTRAINT authorization_token_pkey PRIMARY KEY (id)
);

CREATE TABLE LINKS(
    link_id bigint NOT NULL,
    url character varying(255)  NOT NULL,
    user_user_id bigint,
    CONSTRAINT link_pkey PRIMARY KEY (link_id),
    CONSTRAINT fkcodoxjfu0voebfe08mv9pykt2 FOREIGN KEY (user_user_id)
        REFERENCES users (user_id) MATCH SIMPLE
		ON UPDATE NO ACTION
		ON DELETE NO ACTION
);

CREATE TABLE TAGS(
    link_id bigint NOT NULL,
    tag character varying(255) ,
    CONSTRAINT fk4mqojfl4jdl0uji1i3p1sst0i FOREIGN KEY (link_id)
        REFERENCES links (link_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);