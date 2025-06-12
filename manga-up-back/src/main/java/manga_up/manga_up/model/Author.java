package manga_up.manga_up.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents an author in the manga system.
 * Contains personal details and associated mangas.
 */
@Entity
@Table(name = "author")
public class Author {

    /**
     * Unique identifier for the author.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_authors", nullable = false)
    private Integer id;

    /**
     * Last name of the author.
     */
    @Size(max = 100)
    @NotNull
    @Column(name = "lastname", nullable = false, length = 100)
    private String lastname;

    /**
     * First name of the author.
     */
    @Size(max = 50)
    @NotNull
    @Column(name = "firstname", nullable = false, length = 50)
    private String firstname;

    /**
     * A detailed description or biography of the author.
     */
    @NotNull
    @Lob
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    /**
     * Optional URL pointing to the author's external profile or image.
     */
    @Size(max = 255)
    @Column(name = "url")
    private String url;

    /**
     * Birthdate of the author.
     */
    @Column(name = "birthdate")
    private LocalDate birthdate;

    /**
     * Date when the author entity was created in the system.
     */
    @Column(name = "created_at")
    private LocalDate createdAt;

    /**
     * Genre usually associated with this author (e.g., Shonen, Seinen).
     */
    @Size(max = 50)
    @Column(name = "genre", nullable = false, length = 50)
    private String genre;

    /**
     * Set of mangas authored by this author.
     */
    @ManyToMany
    @JoinTable(name = "mangas_authors", joinColumns = @JoinColumn(name = "Id_authors"), inverseJoinColumns = @JoinColumn(name = "Id_mangas"))
    private Set<Manga> mangas = new LinkedHashSet<>();

    // Getters and setters with no need for Javadoc unless extra logic is added

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Manga> getMangas() {
        return mangas;
    }

    public void setMangas(Set<Manga> mangas) {
        this.mangas = mangas;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
