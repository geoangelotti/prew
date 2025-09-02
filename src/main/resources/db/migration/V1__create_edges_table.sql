CREATE TABLE edges (
    from_id INTEGER NOT NULL,
    to_id INTEGER NOT NULL,
    CONSTRAINT pk_edges PRIMARY KEY (from_id, to_id)
);

-- index
CREATE INDEX idx_edges_from_id ON edges(from_id);