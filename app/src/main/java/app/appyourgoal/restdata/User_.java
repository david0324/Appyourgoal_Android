package app.appyourgoal.restdata;

/**
 * Created by Dragisa on 11/4/2015.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User_ {

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
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("account_type")
    @Expose
    private String accountType;
    @SerializedName("api_endpoint")
    @Expose
    private String apiEndpoint;

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

    public User_ withUserId(String userId) {
        this.userId = userId;
        return this;
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

    public User_ withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
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

    public User_ withLastName(String lastName) {
        this.lastName = lastName;
        return this;
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

    public User_ withEmail(String email) {
        this.email = email;
        return this;
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

    public User_ withProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
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

    public User_ withNationality(String nationality) {
        this.nationality = nationality;
        return this;
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

    public User_ withClubName(String clubName) {
        this.clubName = clubName;
        return this;
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

    public User_ withStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public User_ withPassword(String password) {
        this.password = password;
        return this;
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

    public User_ withRole(String role) {
        this.role = role;
        return this;
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

    public User_ withAccountType(String accountType) {
        this.accountType = accountType;
        return this;
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

    public User_ withApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
        return this;
    }

}
