package model;

/** One file (document, image, text, etc.) uploaded by a lecturer for a course. */
public class LectureMaterial {
    private final int id;
    private final int courseId;
    private final String courseCode;
    private final String courseName;
    private final String title;
    private final String fileName;
    private final String storedPath;
    private final int uploadedBy;
    private final String uploadedAt;

    public LectureMaterial(int id, int courseId, String courseCode, String courseName,
                           String title, String fileName, String storedPath,
                           int uploadedBy, String uploadedAt) {
        this.id = id;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.title = title;
        this.fileName = fileName;
        this.storedPath = storedPath;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
    }

    public int getId() { return id; }
    public int getCourseId() { return courseId; }
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public String getTitle() { return title; }
    public String getFileName() { return fileName; }
    public String getStoredPath() { return storedPath; }
    public int getUploadedBy() { return uploadedBy; }
    public String getUploadedAt() { return uploadedAt; }
}
