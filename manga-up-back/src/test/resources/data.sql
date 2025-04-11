CREATE TABLE gender_user(
                            Id_genders_user INT AUTO_INCREMENT,
                            label VARCHAR(50) NOT NULL,
                            CONSTRAINT gender_user_PK PRIMARY KEY(Id_genders_user)
);

CREATE TABLE user_address(
                             Id_user_address INT AUTO_INCREMENT,
                             line1 VARCHAR(50) NOT NULL,
                             line2 VARCHAR(50),
                             line3 VARCHAR(50),
                             city VARCHAR(100) NOT NULL,
                             created_at DATETIME,
                             postal_code VARCHAR(5) NOT NULL,
                             CONSTRAINT user_address_PK PRIMARY KEY(Id_user_address)
);

CREATE TABLE author(
                       Id_authors INT AUTO_INCREMENT,
                       lastname VARCHAR(100) NOT NULL,
                       firstname VARCHAR(50) NOT NULL,
                       description TEXT NOT NULL,
                       created_at DATE,
                       CONSTRAINT author_PK PRIMARY KEY(Id_authors)
);

CREATE TABLE genre(
                      Id_gender_mangas INT AUTO_INCREMENT,
                      label VARCHAR(50) NOT NULL,
                      created_at DATETIME,
                      CONSTRAINT genre_PK PRIMARY KEY(Id_gender_mangas)
);

CREATE TABLE category(
                         Id_categories INT AUTO_INCREMENT,
                         label VARCHAR(50) NOT NULL,
                         description TEXT NOT NULL,
                         created_at DATETIME,
                         CONSTRAINT category_PK PRIMARY KEY(Id_categories)
);

CREATE TABLE means_of_payment(
                                 Id_means_of_payment INT AUTO_INCREMENT,
                                 label VARCHAR(50),
                                 CONSTRAINT means_of_payment_PK PRIMARY KEY(Id_means_of_payment)
);

CREATE TABLE status(
                       Id_status INT AUTO_INCREMENT,
                       label VARCHAR(50),
                       CONSTRAINT status_PK PRIMARY KEY(Id_status)
);

CREATE TABLE app_user(
                         Id_users INT AUTO_INCREMENT,
                         username VARCHAR(50) NOT NULL,
                         firstname VARCHAR(80) NOT NULL,
                         lastname VARCHAR(80) NOT NULL,
                         role VARCHAR(10),
                         phone_number VARCHAR(15),
                         email VARCHAR(320) NOT NULL,
                         created_at DATE,
                         password VARCHAR(128) NOT NULL,
                         Id_user_address INT NOT NULL,
                         Id_genders_user INT NOT NULL,
                         CONSTRAINT app_user_PK PRIMARY KEY(Id_users),
                         CONSTRAINT app_user_AK UNIQUE(username),
                         CONSTRAINT app_user_user_address_FK FOREIGN KEY(Id_user_address) REFERENCES user_address(Id_user_address),
                         CONSTRAINT app_user_gender_user_FK FOREIGN KEY(Id_genders_user) REFERENCES gender_user(Id_genders_user)
);

CREATE TABLE cart(
                     Id_cart INT AUTO_INCREMENT,
                     date_created DATETIME,
                     validation_date DATETIME,
                     invoice_date DATETIME,
                     Id_status INT NOT NULL,
                     Id_means_of_payment INT NOT NULL,
                     Id_user_address INT NOT NULL,
                     Id_users INT NOT NULL,
                     CONSTRAINT cart_PK PRIMARY KEY(Id_cart),
                     CONSTRAINT cart_status_FK FOREIGN KEY(Id_status) REFERENCES status(Id_status) ON DELETE CASCADE ON UPDATE CASCADE,
                     CONSTRAINT cart_means_of_payment_FK FOREIGN KEY(Id_means_of_payment) REFERENCES means_of_payment(Id_means_of_payment),
                     CONSTRAINT cart_user_address_FK FOREIGN KEY(Id_user_address) REFERENCES user_address(Id_user_address),
                     CONSTRAINT cart_app_user_FK FOREIGN KEY(Id_users) REFERENCES app_user(Id_users)
);

CREATE TABLE manga(
                      Id_mangas INT AUTO_INCREMENT,
                      title VARCHAR(255) NOT NULL,
                      subtitle VARCHAR(255),
                      release_date DATETIME,
                      summary VARCHAR(500),
                      price DECIMAL(5,2),
                      price_ht DECIMAL(5,2),
                      in_stock BOOLEAN,
                      active BOOLEAN,
                      Id_categories INT NOT NULL,
                      CONSTRAINT manga_PK PRIMARY KEY(Id_mangas),
                      CONSTRAINT manga_category_FK FOREIGN KEY(Id_categories) REFERENCES category(Id_categories) ON DELETE CASCADE
);

CREATE TABLE comment(
                        Id_comment INT AUTO_INCREMENT,
                        rating INT,
                        comment TEXT,
                        created_at DATETIME,
                        Id_mangas INT NOT NULL,
                        Id_users INT NOT NULL,
                        CONSTRAINT comment_PK PRIMARY KEY(Id_comment),
                        CONSTRAINT comment_manga_FK FOREIGN KEY(Id_mangas) REFERENCES manga(Id_mangas) ON DELETE CASCADE,
                        CONSTRAINT comment_app_user_FK FOREIGN KEY(Id_users) REFERENCES app_user(Id_users) ON DELETE CASCADE
);

CREATE TABLE picture(
                        Id_picture INT AUTO_INCREMENT,
                        name VARCHAR(50),
                        url VARCHAR(255),
                        Id_mangas INT NOT NULL,
                        CONSTRAINT picture_PK PRIMARY KEY(Id_picture),
                        CONSTRAINT picture_manga_FK FOREIGN KEY(Id_mangas) REFERENCES manga(Id_mangas) ON DELETE CASCADE
);

CREATE TABLE user_picture(
                             Id_user_picture INT AUTO_INCREMENT,
                             url VARCHAR(2083),
                             Id_users INT NOT NULL,
                             CONSTRAINT user_picture_PK PRIMARY KEY(Id_user_picture),
                             CONSTRAINT user_picture_AK UNIQUE(Id_users),
                             CONSTRAINT user_picture_app_user_FK FOREIGN KEY(Id_users) REFERENCES app_user(Id_users) ON DELETE CASCADE
);

CREATE TABLE manga_cart(
                           Id_cart INT,
                           Id_mangas INT,
                           quantity INT,
                           CONSTRAINT manga_cart_PK PRIMARY KEY(Id_cart, Id_mangas),
                           CONSTRAINT manga_cart_cart_FK FOREIGN KEY(Id_cart) REFERENCES cart(Id_cart),
                           CONSTRAINT manga_cart_manga_FK FOREIGN KEY(Id_mangas) REFERENCES manga(Id_mangas)
);

CREATE TABLE mangas_authors(
                               Id_mangas INT,
                               Id_authors INT,
                               CONSTRAINT mangas_authors_PK PRIMARY KEY(Id_mangas, Id_authors),
                               CONSTRAINT mangas_authors_manga_FK FOREIGN KEY(Id_mangas) REFERENCES manga(Id_mangas),
                               CONSTRAINT mangas_authors_author_FK FOREIGN KEY(Id_authors) REFERENCES author(Id_authors)
);

CREATE TABLE genres_manga(
                             Id_mangas INT,
                             Id_gender_mangas INT,
                             CONSTRAINT genres_manga_PK PRIMARY KEY(Id_mangas, Id_gender_mangas),
                             CONSTRAINT genres_manga_manga_FK FOREIGN KEY(Id_mangas) REFERENCES manga(Id_mangas),
                             CONSTRAINT genres_manga_genre_FK FOREIGN KEY(Id_gender_mangas) REFERENCES genre(Id_gender_mangas) ON DELETE CASCADE
);


--Insert Data
-- Gender User
INSERT INTO gender_user(label)
VALUES ( 'Homme' ),
       ('Femme'),
       ('Autre');

--User address
INSERT INTO user_address( line1,line2,line3,city, postal_code)
VALUES ('12 rue des Lilas', '', '', 'Paris', '75012'),
       ('45 Avenue Victor Hugo', 'Bâtiment B', '3e étage', 'Lyon', '69006'),
       ('89 boulevard de la Liberté', '', '', 'Marseille', '13001'),
       ('7 chemin des Vignes', 'Appartement 202', '', 'Toulouse', '31000'),
       ('23 rue du Château', '', 'Escalier A', 'Bordeaux', '33000'),
       ('5 place de la République', '', '', 'Nantes', '44000'),
       ('18 rue Saint-Michel', 'Résidence les Marronniers', '', 'Rennes', '35000'),
       ('102 avenue Jean Jaurès', '', '', 'Lille', '59000'),
       ('66 allée des Acacias', '', '', 'Nice', '06000'),
       ('14 rue des Écoles', 'Bât C', '2e étage', 'Strasbourg', '67000');

--Author address
INSERT INTO author(lastname , firstname, description ,created_at )
    VALUES
    ('Toriyama', 'Akira', 'Mangaka japonais célèbre pour avoir créé "Dragon Ball" et "Dr. Slump".', '2022-07-12'),
    ('Oda', 'Eiichiro', 'Auteur de "One Piece", l’un des mangas les plus vendus de l’histoire.', '2023-01-25'),
    ('Kishimoto', 'Masashi', 'Créateur de "Naruto", manga culte mêlant ninjas et aventures épiques.', '2023-05-18'),
    ('Togashi', 'Yoshihiro', 'Connu pour "Hunter x Hunter" et "Yu Yu Hakusho", mangaka à l’imaginaire unique.', '2024-02-09'),
    ('Arakawa', 'Hiromu', 'Auteure de "Fullmetal Alchemist", célèbre pour ses intrigues profondes.', '2022-09-30'),
    ('Isayama', 'Hajime', 'Créateur de "L’Attaque des Titans", une œuvre post-apocalyptique marquante.', '2023-11-03'),
    ('Inoue', 'Takehiko', 'Auteur de "Slam Dunk" et "Vagabond", maître du trait réaliste.', '2022-04-20'),
    ('Gotouge', 'Koyoharu', 'Auteure de "Demon Slayer", succès mondial au style percutant.', '2024-03-14');

INSERT INTO category (label, description, created_at)
VALUES
    ('Action', 'Catégorie centrée sur les scènes d’action et les aventures intenses.', '2023-02-14 09:00:00'),
    ('Comédie', 'Manga axé sur l’humour et les situations comiques.', '2024-01-05 16:30:00');

INSERT INTO genre (label, created_at)
VALUES
        ('Shonen', '2023-01-01 08:00:00'),
        ('Seinen', '2022-09-10 10:00:00');

INSERT INTO status (label) VALUES
                               ('En cours'),
                               ('Validée'),
                               ('Annulée');

INSERT INTO means_of_payment (label)
VALUES
    ('Carte bancaire'),
    ('PayPal');


INSERT INTO manga (title, subtitle, release_date, summary, price, price_ht, in_stock, active, Id_categories)
VALUES
    ('Dragon Ball', 'Un voyage épique', '2023-03-01 00:00:00', 'L’histoire de Goku et ses amis dans un monde fantastique.', 20.00, 18.00, TRUE, TRUE, 1),
    ('One Piece', 'Les aventures de Luffy', '2023-04-15 00:00:00', 'Luffy et son équipage à la recherche du trésor légendaire.', 25.00, 22.00, TRUE, TRUE, 1);

INSERT INTO app_user (username, firstname, lastname, role, phone_number, email, created_at, password, Id_user_address, Id_genders_user)
VALUES
    ('johndoe', 'John', 'Doe', 'user', '0123456789', 'johndoe@example.com', '2023-04-10', 'password123', 1, 1),
    ('janedoe', 'Jane', 'Doe', 'admin', '0987654321', 'janedoe@example.com', '2023-02-20', 'password456', 2, 2);

INSERT INTO comment (rating, comment, created_at, Id_mangas, Id_users)
VALUES
    (5, 'Un manga exceptionnel, très captivant!', '2023-04-10 10:00:00', 1, 1),
    (4, 'Bien écrit, mais parfois un peu long.', '2023-03-20 14:30:00', 2, 2);
