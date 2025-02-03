## **Music Metadata Service Documentation**

### **Overview**
The **Music Metadata Service** is a backend service designed to store and manage metadata about music tracks and artists. It provides a RESTful API for adding artists, adding tracks, editing artist names, fetching tracks, and displaying a rotating "Artist of the Day."

---

### **Features**
1. **Add a New Artist**:
   - Users can add a new artist with a unique ID and name.

2. **Add a New Track**:
   - Users can add a new track to an artist's catalog, including attributes like title, genre, and length.

3. **Edit Artist Name**:
   - Users can update an artist's name.

4. **Fetch Artist Tracks**:
   - Users can fetch all tracks associated with a specific artist, with optional pagination.

5. **Artist of the Day**:
   - The service rotates through all artists in the catalog, displaying a different "Artist of the Day" each day.

6. **Search Artists**:
   - Users can search for artists by name.

---

### **Technologies Used**
- **Scala**: Primary programming language.
- **Akka HTTP**: For building the RESTful API.
- **ScalaTest**: For unit testing.
- **Spray JSON**: For JSON serialization and deserialization.

---

### **Setup Instructions**

#### **1. Prerequisites**
- **Java Development Kit (JDK)**: Version 11 or higher.
- **sbt (Scala Build Tool)**: For building and running the project.

#### **2. Clone the Repository**
```bash
git clone <repository-url>
cd music-metadata-service
```

#### **3. Build the Project**
```bash
sbt compile
```

#### **4. Run the Application**
```bash
sbt run
```
The server will start at `http://localhost:8080/`.

#### **5. Run Tests**
```bash
sbt test
```

---

### **API Documentation**

#### **Base URL**
```
http://localhost:8080
```

#### **Endpoints**

1. **Add a New Artist**
   - **Method**: `POST`
   - **URL**: `/artists`
   - **Request Body**:
     ```json
     {
       "artistId": "artist123",
       "name": "Artist Name"
     }
     ```
   - **Response**:
     ```
     "Artist added successfully"
     ```

2. **Add a New Track**
   - **Method**: `POST`
   - **URL**: `/artists/{artistId}/tracks`
   - **Request Body**:
     ```json
     {
       "id": "track1",
       "title": "Song 1",
       "genre": "Pop",
       "length": 240
     }
     ```
   - **Response**:
     ```
     "track1"
     ```

3. **Edit Artist Name**
   - **Method**: `PUT`
   - **URL**: `/artists/{artistId}`
   - **Request Body**:
     ```json
     {
       "name": "New Artist Name"
     }
     ```
   - **Response**:
     ```
     "Artist name updated"
     ```

4. **Fetch Artist Tracks**
   - **Method**: `GET`
   - **URL**: `/artists/{artistId}/tracks`
   - **Query Parameters**:
     - `page` (optional): Page number for pagination.
     - `pageSize` (optional): Number of tracks per page.
   - **Response**:
     ```json
     [
       {
         "id": "track1",
         "title": "Song 1",
         "genre": "Pop",
         "length": 240
       }
     ]
     ```

5. **Fetch Artist Details**
   - **Method**: `GET`
   - **URL**: `/artists/{artistId}`
   - **Response**:
     ```json
     {
       "id": "artist123",
       "name": "Artist Name",
       "tracks": [
         {
           "id": "track1",
           "title": "Song 1",
           "genre": "Pop",
           "length": 240
         }
       ]
     }
     ```

6. **Get Artist of the Day**
   - **Method**: `GET`
   - **URL**: `/artist-of-the-day`
   - **Response**:
     ```json
     {
       "id": "artist123",
       "name": "Artist Name",
       "tracks": []
     }
     ```

7. **Search Artists**
   - **Method**: `GET`
   - **URL**: `/search`
   - **Query Parameters**:
     - `query`: Search term.
   - **Response**:
     ```json
     [
       {
         "id": "artist123",
         "name": "Artist Name",
         "tracks": []
       }
     ]
     ```

---

### **Design Decisions**

1. **In-Memory Storage**:
   - The service uses an in-memory `Map` to store artists and tracks for simplicity. In a production environment, this would be replaced with a persistent database like PostgreSQL or MongoDB.

2. **Pagination**:
   - The `GET /artists/{artistId}/tracks` endpoint supports optional pagination to handle large datasets efficiently.

3. **Cyclical Artist of the Day**:
   - The "Artist of the Day" feature cycles through all artists in the catalog using a modulo operation to ensure fair rotation.

4. **Error Handling**:
   - The service returns meaningful error messages and HTTP status codes for invalid requests (e.g., non-existent artist, invalid track data).

5. **Input Validation**:
   - All inputs (e.g., artist name, track data) are validated to ensure data integrity.

---

### **Future Improvements**
1. **Database Integration**:
   - Replace in-memory storage with a persistent database like PostgreSQL or MongoDB.

2. **Authentication and Authorization**:
   - Add user authentication and role-based access control.

3. **Caching**:
   - Use a caching mechanism (e.g., Redis) to improve performance for frequently accessed data.

4. **Advanced Search**:
   - Implement full-text search for artists and tracks using a search engine like Elasticsearch.

5. **Deployment**:
   - Containerize the application using Docker and deploy it to a cloud platform like AWS or Google Cloud.

---

### **Example API Requests**

#### **Add an Artist**
```bash
curl -X POST -H "Content-Type: application/json" -d '{"artistId": "artist123", "name": "Artist Name"}' http://localhost:8080/artists
```

#### **Add a Track**
```bash
curl -X POST -H "Content-Type: application/json" -d '{"id": "track1", "title": "Song 1", "genre": "Pop", "length": 240}' http://localhost:8080/artists/artist123/tracks
```

#### **Fetch Artist Tracks**
```bash
curl http://localhost:8080/artists/artist123/tracks
```

#### **Get Artist of the Day**
```bash
curl http://localhost:8080/artist-of-the-day
```
