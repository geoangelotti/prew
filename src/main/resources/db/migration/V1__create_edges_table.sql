CREATE TABLE edges (
    from_id INTEGER NOT NULL,
    to_id INTEGER NOT NULL,
    CONSTRAINT edge_unique UNIQUE (from_id, to_id)
);

-- index
CREATE INDEX idx_edges_from_id ON edges(from_id);