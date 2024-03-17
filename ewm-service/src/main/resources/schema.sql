CREATE TABLE IF NOT EXISTS users (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(254) NOT NULL,
    name VARCHAR(250) NOT NULL,
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT uq_cat_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation VARCHAR(2000) NOT NULL,
    category_id INT NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id INT NOT NULL,
    is_paid BOOLEAN NOT NULL,
    title VARCHAR(120) NOT NULL,
    loc_lat DOUBLE PRECISION NOT NULL,
    loc_lon DOUBLE PRECISION NOT NULL,
    participant_limit INT NOT NULL,
    request_moderation BOOLEAN NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    state VARCHAR NOT NULL,
    confirmed_requests INTEGER,
    CONSTRAINT fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(id),
    CONSTRAINT fk_events_to_users FOREIGN KEY(initiator_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS requests (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    event_id INT NOT NULL,
    requester_id INT NOT NULL,
    status VARCHAR NOT NULL,
    CONSTRAINT fk_requests_to_events FOREIGN KEY(event_id) REFERENCES events(id),
    CONSTRAINT fk_requests_to_users FOREIGN KEY(requester_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    is_pinned BOOLEAN NOT NULL,
    title VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id INT NOT NULL,
    event_id INT NOT NULL,
    CONSTRAINT compilation_events_pk PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk_comp_events_to_comps FOREIGN KEY(compilation_id) REFERENCES compilations(id),
    CONSTRAINT fk_comp_events_to_events FOREIGN KEY(event_id) REFERENCES events(id)
);