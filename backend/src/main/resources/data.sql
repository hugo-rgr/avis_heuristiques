-- ============================================================
-- SEED — Données initiales
-- Modérateur : email=modo@avis.fr  motDePasse=Admin1234!
-- ============================================================

-- Classifications PEGI
INSERT INTO classifications (nom, couleurrgb) VALUES ('PEGI 3',  '#59A608');
INSERT INTO classifications (nom, couleurrgb) VALUES ('PEGI 7',  '#59A608');
INSERT INTO classifications (nom, couleurrgb) VALUES ('PEGI 12', '#F4A000');
INSERT INTO classifications (nom, couleurrgb) VALUES ('PEGI 16', '#EE7203');
INSERT INTO classifications (nom, couleurrgb) VALUES ('PEGI 18', '#D0021B');

-- Genres
INSERT INTO genres (nom) VALUES ('Action-Aventure');
INSERT INTO genres (nom) VALUES ('RPG');
INSERT INTO genres (nom) VALUES ('FPS');
INSERT INTO genres (nom) VALUES ('Sport');
INSERT INTO genres (nom) VALUES ('Simulation');
INSERT INTO genres (nom) VALUES ('Stratégie');
INSERT INTO genres (nom) VALUES ('Plateforme');

-- Éditeurs
INSERT INTO editeurs (nom) VALUES ('Nintendo');
INSERT INTO editeurs (nom) VALUES ('CD Projekt RED');
INSERT INTO editeurs (nom) VALUES ('Rockstar Games');
INSERT INTO editeurs (nom) VALUES ('Ubisoft');
INSERT INTO editeurs (nom) VALUES ('Electronic Arts');
INSERT INTO editeurs (nom) VALUES ('FromSoftware');
INSERT INTO editeurs (nom) VALUES ('Naughty Dog');

-- Plateformes
INSERT INTO plateformes (nom, date_de_sortie) VALUES ('PC',              '1981-01-01');
INSERT INTO plateformes (nom, date_de_sortie) VALUES ('PlayStation 5',   '2020-11-12');
INSERT INTO plateformes (nom, date_de_sortie) VALUES ('Xbox Series X',   '2020-11-10');
INSERT INTO plateformes (nom, date_de_sortie) VALUES ('Nintendo Switch',  '2017-03-03');
INSERT INTO plateformes (nom, date_de_sortie) VALUES ('PlayStation 4',   '2013-11-15');

-- Modérateur (motDePasse = Admin1234!)
INSERT INTO moderateurs (pseudo, email, mot_de_passe, numero_de_telephone)
VALUES ('modo', 'modo@avis.fr', '$2a$10$bScZkGs91cli9mlHvsAjE.95NtzP7vP3zjuKSiIXVVHNsjgTgI7nu', '0600000000');

-- ============================================================
-- Jeux
-- ============================================================

INSERT INTO jeux (nom, description, image, prix, date_de_sortie, genre_id, editeur_id, classification_id)
VALUES (
  'The Legend of Zelda: Breath of the Wild',
  'Un immense monde ouvert où Link explore Hyrule, résout des énigmes dans des sanctuaires et affronte Calamity Ganon. Une liberté totale dans chaque instant.',
  'https://gaming-cdn.com/images/products/2616/616x353/the-legend-of-zelda-breath-of-the-wild-switch-jeu-nintendo-eshop-europe-cover.jpg',
  49.99, '2017-03-03',
  (SELECT id FROM genres WHERE nom = 'Action-Aventure'),
  (SELECT id FROM editeurs WHERE nom = 'Nintendo'),
  (SELECT id FROM classifications WHERE nom = 'PEGI 12')
);

INSERT INTO jeux (nom, description, image, prix, date_de_sortie, genre_id, editeur_id, classification_id)
VALUES (
  'The Witcher 3: Wild Hunt',
  'Geralt de Riv parcourt un monde en guerre pour retrouver sa fille adoptive. Un RPG colossal récompensé par plus de 250 Game of the Year awards.',
  'https://upload.wikimedia.org/wikipedia/en/0/0c/Witcher_3_cover_art.jpg',
  29.99, '2015-05-19',
  (SELECT id FROM genres WHERE nom = 'RPG'),
  (SELECT id FROM editeurs WHERE nom = 'CD Projekt RED'),
  (SELECT id FROM classifications WHERE nom = 'PEGI 18')
);

INSERT INTO jeux (nom, description, image, prix, date_de_sortie, genre_id, editeur_id, classification_id)
VALUES (
  'Grand Theft Auto V',
  'Trois criminels naviguent dans Los Santos, ville tentaculaire rongée par la corruption. L''un des jeux les plus vendus de l''histoire.',
  'https://www.weareplaystation.fr/cdn-cgi/image/w=3840,f=auto,q=80/api/upload/media/post/0001/05/thumb_4497_post_big.png',
  29.99, '2013-09-17',
  (SELECT id FROM genres WHERE nom = 'Action-Aventure'),
  (SELECT id FROM editeurs WHERE nom = 'Rockstar Games'),
  (SELECT id FROM classifications WHERE nom = 'PEGI 18')
);

INSERT INTO jeux (nom, description, image, prix, date_de_sortie, genre_id, editeur_id, classification_id)
VALUES (
  'Elden Ring',
  'Un RPG en monde ouvert créé avec George R.R. Martin. Explorez les Terres Intermédiaires et affrontez des boss d''une difficulté légendaire.',
  'https://upload.wikimedia.org/wikipedia/en/b/b9/Elden_Ring_Box_art.jpg',
  59.99, '2022-02-25',
  (SELECT id FROM genres WHERE nom = 'RPG'),
  (SELECT id FROM editeurs WHERE nom = 'FromSoftware'),
  (SELECT id FROM classifications WHERE nom = 'PEGI 16')
);

INSERT INTO jeux (nom, description, image, prix, date_de_sortie, genre_id, editeur_id, classification_id)
VALUES (
  'The Last of Us Part II',
  'Ellie part en quête de vengeance dans un monde post-apocalyptique. Une narration brutale et maîtrisée, un chef-d''œuvre du jeu vidéo.',
  'https://image.api.playstation.com/vulcan/ap/rnd/202206/0720/eEczyEMDd2BLa3dtkGJVE9Id.png',
  39.99, '2020-06-19',
  (SELECT id FROM genres WHERE nom = 'Action-Aventure'),
  (SELECT id FROM editeurs WHERE nom = 'Naughty Dog'),
  (SELECT id FROM classifications WHERE nom = 'PEGI 18')
);

INSERT INTO jeux (nom, description, image, prix, date_de_sortie, genre_id, editeur_id, classification_id)
VALUES (
  'Mario Kart 8 Deluxe',
  'La référence du kart racing avec 48 circuits, des modes en ligne compétitifs et un gameplay accessible à tous les âges.',
  'https://images.lanouvellerepublique.fr/image/upload/t_1020w/f_auto/684a919511dc13a55f8b45b3.jpg',
  49.99, '2017-04-28',
  (SELECT id FROM genres WHERE nom = 'Sport'),
  (SELECT id FROM editeurs WHERE nom = 'Nintendo'),
  (SELECT id FROM classifications WHERE nom = 'PEGI 3')
);

INSERT INTO jeux (nom, description, image, prix, date_de_sortie, genre_id, editeur_id, classification_id)
VALUES (
  'Cyberpunk 2077',
  'Night City, 2077. V, mercenaire ambitieux, se retrouve avec l''implant d''une légende. Un RPG immersif dans un monde cyberpunk dense et vivant.',
  'https://upload.wikimedia.org/wikipedia/en/9/9f/Cyberpunk_2077_box_art.jpg',
  39.99, '2020-12-10',
  (SELECT id FROM genres WHERE nom = 'RPG'),
  (SELECT id FROM editeurs WHERE nom = 'CD Projekt RED'),
  (SELECT id FROM classifications WHERE nom = 'PEGI 18')
);

INSERT INTO jeux (nom, description, image, prix, date_de_sortie, genre_id, editeur_id, classification_id)
VALUES (
  'FIFA 23',
  'La dernière édition FIFA avec HyperMotion2, des clubs de football féminin et des modes Ultimate Team enrichis.',
  'https://sm.ign.com/t/ign_nl/cover/f/fifa-23-le/fifa-23-legacy-edition_yaw3.600.jpg',
  29.99, '2022-09-30',
  (SELECT id FROM genres WHERE nom = 'Sport'),
  (SELECT id FROM editeurs WHERE nom = 'Electronic Arts'),
  (SELECT id FROM classifications WHERE nom = 'PEGI 3')
);

-- ============================================================
-- Associations Jeu ↔ Plateforme
-- ============================================================

-- Zelda → Switch
INSERT INTO jeu_plateforme (jeu_id, plateforme_id)
VALUES (
  (SELECT id FROM jeux WHERE nom = 'The Legend of Zelda: Breath of the Wild'),
  (SELECT id FROM plateformes WHERE nom = 'Nintendo Switch')
);

-- Witcher 3 → PC, PS4, PS5
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'The Witcher 3: Wild Hunt'), (SELECT id FROM plateformes WHERE nom = 'PC'));
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'The Witcher 3: Wild Hunt'), (SELECT id FROM plateformes WHERE nom = 'PlayStation 4'));
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'The Witcher 3: Wild Hunt'), (SELECT id FROM plateformes WHERE nom = 'PlayStation 5'));

-- GTA V → PC, PS4, PS5
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'Grand Theft Auto V'), (SELECT id FROM plateformes WHERE nom = 'PC'));
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'Grand Theft Auto V'), (SELECT id FROM plateformes WHERE nom = 'PlayStation 4'));
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'Grand Theft Auto V'), (SELECT id FROM plateformes WHERE nom = 'PlayStation 5'));

-- Elden Ring → PC, PS5, Xbox Series X
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'Elden Ring'), (SELECT id FROM plateformes WHERE nom = 'PC'));
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'Elden Ring'), (SELECT id FROM plateformes WHERE nom = 'PlayStation 5'));
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'Elden Ring'), (SELECT id FROM plateformes WHERE nom = 'Xbox Series X'));

-- The Last of Us Part II → PS4, PS5
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'The Last of Us Part II'), (SELECT id FROM plateformes WHERE nom = 'PlayStation 4'));
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'The Last of Us Part II'), (SELECT id FROM plateformes WHERE nom = 'PlayStation 5'));

-- Mario Kart → Switch
INSERT INTO jeu_plateforme (jeu_id, plateforme_id)
VALUES (
  (SELECT id FROM jeux WHERE nom = 'Mario Kart 8 Deluxe'),
  (SELECT id FROM plateformes WHERE nom = 'Nintendo Switch')
);

-- Cyberpunk → PC, PS4, PS5, Xbox Series X
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'Cyberpunk 2077'), (SELECT id FROM plateformes WHERE nom = 'PC'));
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'Cyberpunk 2077'), (SELECT id FROM plateformes WHERE nom = 'PlayStation 4'));
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'Cyberpunk 2077'), (SELECT id FROM plateformes WHERE nom = 'PlayStation 5'));
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'Cyberpunk 2077'), (SELECT id FROM plateformes WHERE nom = 'Xbox Series X'));

-- FIFA 23 → PC, PS4, PS5, Xbox Series X
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'FIFA 23'), (SELECT id FROM plateformes WHERE nom = 'PC'));
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'FIFA 23'), (SELECT id FROM plateformes WHERE nom = 'PlayStation 4'));
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'FIFA 23'), (SELECT id FROM plateformes WHERE nom = 'PlayStation 5'));
INSERT INTO jeu_plateforme (jeu_id, plateforme_id) VALUES ((SELECT id FROM jeux WHERE nom = 'FIFA 23'), (SELECT id FROM plateformes WHERE nom = 'Xbox Series X'));
