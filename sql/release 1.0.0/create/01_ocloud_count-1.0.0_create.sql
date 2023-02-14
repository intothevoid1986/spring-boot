-- Table: public.cluster

-- DROP TABLE IF EXISTS public.cluster;

CREATE TABLE IF NOT EXISTS public.cluster
(
    id bigint NOT NULL DEFAULT nextval('cluster_id_seq'::regclass),
    cluster_name character varying(255) COLLATE pg_catalog."default",
    service character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT cluster_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.cluster
    OWNER to ocloud_cost;
	

-- Table: public.cluster

-- INSERT TABLE public.cluster;

INSERT INTO public.cluster(
	id, cluster_name, service)
	VALUES (1, 'ocloud-staging', 'kuberbetes'),
    (2, 'ocloud-prod', 'kuberbetes');


-- Table: public.image

-- DROP TABLE IF EXISTS public.image;

CREATE TABLE IF NOT EXISTS public.image
(
    id bigint NOT NULL DEFAULT nextval('image_id_seq'::regclass),
    image_ref character varying(255) COLLATE pg_catalog."default",
    service character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT image_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.image
    OWNER to ocloud_cost;


-- Table: public.image

-- INSERT TABLE public.image;
INSERT INTO public.image(
	id, image_ref, service)
	VALUES (1, '5ebcca42-c40a-4c93-8a7f-3c58798d4194', 'kubernetes'),
    (2,'2fb7cde2-a17d-4bb7-bac7-9c22d62c7595', 'kuberbetes'),
    (3, '1f0bc61e-68b1-40a9-96d1-d8469ec5ffaa', 'kuberbetes');


-- Table: public.measure

-- DROP TABLE IF EXISTS public.measure;

CREATE TABLE IF NOT EXISTS public.measure
(
    id bigint NOT NULL,
    granularity double precision,
    "timestamp" timestamp(6) without time zone,
    vcpusnumber integer,
    metrics_vcpus bigint,
    CONSTRAINT measure_pkey PRIMARY KEY (id),
    CONSTRAINT fkfvjo0crb2s3badby0j1rn948l FOREIGN KEY (metrics_vcpus)
        REFERENCES public.metrics (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.measure
    OWNER to ocloud_cost;


-- Table: public.metrics

-- DROP TABLE IF EXISTS public.metrics;

CREATE TABLE IF NOT EXISTS public.metrics
(
    id bigint NOT NULL,
    vcpus character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT metrics_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.metrics
    OWNER to ocloud_cost;


-- Table: public.price

-- DROP TABLE IF EXISTS public.price;

CREATE TABLE IF NOT EXISTS public.price
(
    id bigint NOT NULL,
    flavor_name character varying(255) COLLATE pg_catalog."default",
    hourly_rate double precision,
    CONSTRAINT listino_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.price
    OWNER to ocloud_cost;


-- Table: public.price

-- INSERT TABLE public.price;
INSERT INTO public.price(
	id, flavor_name, hourly_rate)
	VALUES (1, 'e3.standard.x1', 0.011),
    (2, 'e3standard.x2', 0.019),
    (3, 'e3standard.x3', 0.034),
    (4, 'e3standard.x4', 0.062),
    (5, 'e3standard.x5', 0.0111),
    (6, 'e3standard.x6', 0.0199),
    (7, 'e3standard.x7', 0.0359),
    (8, 'e3standard.x8', 0.0646),
    (9, 'e3standard.x9', 1.162);


-- Table: public.resources

-- DROP TABLE IF EXISTS public.resources;

CREATE TABLE IF NOT EXISTS public.resources
(
    id bigint NOT NULL,
    display_name character varying(255) COLLATE pg_catalog."default",
    flavor_name character varying(255) COLLATE pg_catalog."default",
    identifier character varying(255) COLLATE pg_catalog."default",
    image_ref character varying(255) COLLATE pg_catalog."default",
    type character varying(255) COLLATE pg_catalog."default",
    resource_id bigint,
    cluster_id bigint,
    CONSTRAINT resources_pkey PRIMARY KEY (id),
    CONSTRAINT fkaakwe0rlo8w54ov39blfh2qny FOREIGN KEY (resource_id)
        REFERENCES public.metrics (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fklh6jxmnbgvuxwfjqmtmix1edg FOREIGN KEY (cluster_id)
        REFERENCES public.cluster (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.resources
    OWNER to ocloud_cost;


-- Table: public.service

-- DROP TABLE IF EXISTS public.service;

CREATE TABLE IF NOT EXISTS public.service
(
    id double precision NOT NULL,
    type character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT service_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.service
    OWNER to ocloud_cost;


-- Table: public.tenant

-- DROP TABLE IF EXISTS public.tenant;

CREATE TABLE IF NOT EXISTS public.tenant
(
    id bigint NOT NULL,
    enabled boolean,
    tenant_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT tenant_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.tenant
    OWNER to ocloud_cost;


-- Table: public.usage_hour

-- DROP TABLE IF EXISTS public.usage_hour;

CREATE TABLE IF NOT EXISTS public.usage_hour
(
    id bigint NOT NULL,
    cluster_name character varying(255) COLLATE pg_catalog."default",
    cost double precision,
    resource_total integer,
    time_slot timestamp(6) without time zone,
    CONSTRAINT usage_hour_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.usage_hour
    OWNER to ocloud_cost;


-- Table: public.usage_hour_cluster

-- DROP TABLE IF EXISTS public.usage_hour_cluster;

CREATE TABLE IF NOT EXISTS public.usage_hour_cluster
(
    id bigint NOT NULL,
    cluster_name character varying(255) COLLATE pg_catalog."default",
    total_resource integer,
    "timestamp" timestamp(6) without time zone,
    total_cost double precision,
    CONSTRAINT usage_hour_cluster_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.usage_hour_cluster
    OWNER to ocloud_cost;