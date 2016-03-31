package app.appyourgoal.restdata;

/**
 * Created by Dragisa on 11/5/2015.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditData {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("club_name")
    @Expose
    private String clubName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("account_type")
    @Expose
    private String accountType;
    @SerializedName("api_endpoint")
    @Expose
    private String apiEndpoint;
    @SerializedName("videos")
    @Expose
    private List<Video> videos = new ArrayList<Video>();
    @SerializedName("medals")
    @Expose
    private List<Medal> medals = new ArrayList<Medal>();

    /**
     *
     * @return
     * The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     * The first_name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return
     * The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     * The last_name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The profilePicture
     */
    public String getProfilePicture() {
        return profilePicture;
    }

    /**
     *
     * @param profilePicture
     * The profile_picture
     */
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     *
     * @return
     * The nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     *
     * @param nationality
     * The nationality
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     *
     * @return
     * The clubName
     */
    public String getClubName() {
        return clubName;
    }

    /**
     *
     * @param clubName
     * The club_name
     */
    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The role
     */
    public String getRole() {
        return role;
    }

    /**
     *
     * @param role
     * The role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     *
     * @return
     * The accountType
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     *
     * @param accountType
     * The account_type
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     *
     * @return
     * The apiEndpoint
     */
    public String getApiEndpoint() {
        return apiEndpoint;
    }

    /**
     *
     * @param apiEndpoint
     * The api_endpoint
     */
    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    /**
     *
     * @return
     * The videos
     */
    public List<Video> getVideos() {
        return videos;
    }

    /**
     *
     * @param videos
     * The videos
     */
    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    /**
     *
     * @return
     * The medals
     */
    public List<Medal> getMedals() {
        return medals;
    }

    /**
     *
     * @param medals
     * The medals
     */
    public void setMedals(List<Medal> medals) {
        this.medals = medals;
    }

}
