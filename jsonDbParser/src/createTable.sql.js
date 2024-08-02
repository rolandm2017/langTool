// -- Table for Nouns
const nouns = `CREATE TABLE IF NOT EXISTS nouns (
    id SERIAL PRIMARY KEY,
    word VARCHAR(255) NOT NULL UNIQUE,
    gender TEXT,
    definition TEXT,
    part_of_speech VARCHAR(50) DEFAULT 'noun',
    example_sentence TEXT,
    plural_of TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);`

// -- Table for Verbs
const verbs = `CREATE TABLE IF NOT EXISTS verbs (
    id SERIAL PRIMARY KEY,
    word VARCHAR(255) NOT NULL UNIQUE,
    definition TEXT,
    tense VARCHAR(50),  -- e.g., past, present, future
    conjugation TEXT,   -- e.g., "I run, you run, he runs..."
    example_sentence TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);`

// -- Table for Adjectives
const adjectives = `CREATE TABLE IF NOT EXISTS adjectives (
    id SERIAL PRIMARY KEY,
    word VARCHAR(255) NOT NULL UNIQUE,
    definition TEXT,
    degree VARCHAR(50),  -- e.g., positive, comparative, superlative
    example_sentence TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);`

// -- Table for Adverbs
const adverbs = `CREATE TABLE IF NOT EXISTS adverbs (
    id SERIAL PRIMARY KEY,
    word VARCHAR(255) NOT NULL UNIQUE,
    definition TEXT,
    degree VARCHAR(50),  -- e.g., positive, comparative, superlative
    example_sentence TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);`

const names = `CREATE TABLE IF NOT EXISTS names (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    gender VARCHAR(50), -- Updated to VARCHAR to match the type of gender
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);`

const otherType = `CREATE TABLE IF NOT EXISTS others (
    id SERIAL PRIMARY KEY,
    word VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);`

// {
//     adv: 4476,
//     intj: 685,
//     phrase: 548,
//     prefix: 341,
//     num: 194,
//     character: 45,
//     pron: 239,
//     prep: 308,
//     article: 17,
//     symbol: 12,
//     conj: 151,
//     det: 64,
//     proverb: 292,
//     suffix: 339,
//     contraction: 86,
//     particle: 10,
//     prep_phrase: 210,
//     interfix: 6,
//     infix: 3,
//     punct: 3,
//     postp: 2
//   }

const tables = { nouns, verbs, adjectives, adverbs, names, otherType }

export default tables
