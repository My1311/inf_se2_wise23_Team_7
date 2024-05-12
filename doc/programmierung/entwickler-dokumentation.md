## Entwickler Dokumentation

### User Story Features erarbeiten
- Erstellen eines neuen Branches (abgeleitet von Main) mit dem Namen "feature/[User Story Name]'.
- Commited auf den neuen Branch die Codeaenderungen.
- Es muss nach dem MVC-Pattern entwickelt werden
- Hierbei gilt es Domain-Spezifisch (Nach einem Feature) zu entwickeln.
- Beispiel Student
  - Im Model-Ordner Student-Verzeichnis erstellen und dort
    - Model-Klasse Student, DTO-Klasse StudentDTO und Repo: StudentRepository
  - Im Cotroll-Ordner
    - eine Service-Klasse StudentService erstellen
  - Im View-Ordner unter routes eine passende Route erstellen
    - Dort dann eine Klasse fuer die view erstellen
- Achtung
  - Wir benutzen in den View-Klassen (Frontend) immer nur Service-Klassen (Control-Verzeichnis).
    Diese Service-Klassen nutzen dann erst die Domian-Spezifischen Repositorys (StudentRepository).

- Erstelle einen [Merge Request](https://vm-2d21.inf.h-brs.de/inf_se2_wise23_Team_7/inf_se2_wise23_Team_7/-/merge_requests/new) und beantragt das, dass neue Feature auf Main kommt.


### ImageService

1. **`inputStreamToByte(InputStream inputStream)`**: Converts an `InputStream` to a `byte[]`, crucial for creating a `Company` object with a logo from an `Upload` object.

2. **`byteToImage(byte[] imageInByte)`**: Converts a `byte[]` image representation for direct use in Vaadin, e.g., within a `Div`.

### CompanyRepository

- **`findCompanyByUserEmail()`**: Retrieves `Company` objects based on the associated user's email.

### CompanyService

- **`readAllCompanies()`**: Provides all `CompanyDTO` objects for the frontend.

- **`getCompanyDTOByEmail()`**: Retrieves the corresponding DTO object based on the provided email.

- **`addAdvertisement()`**: In progress; aims to find a company and add an advertisement.

- **`createCompany()`**: Creates a new company with the associated user.

- **`getCompanyByEmail()`**: Retrieves a company based on the provided email.

- **`getCompanyDTOByEmail()`**: Retrieves the `CompanyDTO` based on the provided email.

### Company

- Enhanced with user and advertisement features.

- Methods for managing advertisements, including creation, deletion, and retrieval.