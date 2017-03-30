package wizard;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.kohsuke.github.*;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by natha on 3/29/2017.
 */
public class GitHubController
{

    private String username;
    private String personalToken;

    private int numOfRepositories = 10;
    private String webhookURL;
    private static ArrayList<GHRepository> repositories = new ArrayList<GHRepository>();
    private GitHub github;
    private GHOrganization organization;

    public GitHubController(String username, String personalToken, String orgName, int numOfRepositories, String webhookURL) throws Exception
    {

        this.username = username;
        this.personalToken = personalToken;

        this.github = GitHub.connect(username, personalToken);
        this.organization = github.getOrganization(orgName);
        this.numOfRepositories = numOfRepositories;
        this.webhookURL = webhookURL;

        github.checkApiUrlValidity();

    }

    public static void main(String[] args)
    {

        try
        {
            /*
            // Set up the credentials for the API to use using AuthO token with github
            github = GitHub.connect("nathanacooper", "0fdba8e3382f113b231515292393706d352848aa");
            org = github.getOrganization("DevOps-Pipeline");
            DevopsConf.createRepos();
            DevopsConf.setUpRepositories();
            DevopsConf.setUpTeams();
            //HelloWorld.removeRepos();
            //HelloWorld.removeTeams();
            //GHCreateRepositoryBuilder builder = org.createRepository("Test5");
            //builder.create();
            // GHCreateRepositoryBuilder repo = github.createRepository("TestRepo3");
            //GHRepository repo = org.getRepository("Test5");
            //GHTeam team = org.createTeam("Team3", GHOrganization.Permission.PULL);
            //team.add(github.getUser("nathanacooper"));
            //team.add(repo);
            github.checkApiUrlValidity();*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("Hello, World!");
    }

    /**
     * Creates github repositories under a specific organization
     * @throws IOException
     */
    private void createRepos() throws IOException
    {

        for (int i = 1; i <= this.numOfRepositories; i++)
        {

            GHCreateRepositoryBuilder builder = this.organization.createRepository("Repo" + i);
            repositories.add(builder.create());
            System.out.println("Creating repo...");

        }

    }

    /**
     * Populates each repository with the PHP application
     * @throws GitAPIException
     * @throws IOException
     */
    private void setUpRepositories(String path) throws GitAPIException, IOException
    {

        for (int i = 1; i <= this.numOfRepositories; i++)
        {

            Git git = Git.open(new File(path));
            git.push()
                    .setCredentialsProvider( new UsernamePasswordCredentialsProvider(this.username, this.personalToken))
                    .setRemote(String.format("https://github.com/%s/Repo%d.git", this.organization.getName(), i))
                    .setPushAll()
                    .call();
            System.out.println("Setting up repo...");

        }

    }

    /**
     * Creates a webhook for each repository
     * https://developer.github.com/v3/repos/hooks/#create-a-hook
     * @throws GitAPIException
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private void createWebhook(String url) throws GitAPIException, IOException
    {

        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, personalToken.toCharArray());
            }
        });

        HttpURLConnection con = (HttpURLConnection) new URL(String.format("https://api.github.com/orgs/%s/hooks", this.organization.getName())).openConnection();
        String userPassword = this.username + ":" + this.personalToken;
        String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());

        con.setRequestProperty("Authorization", "Basic " + encoding);
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.getOutputStream().write(String.format("{"
                                                + "`name`: `web`,"
                                                + "`active`: true,"
                                                + "`events`: [`push`],"
                                                + "config: {"
                                                + "`url`: `%s`"
                                                + "}", this.webhookURL).replace('`', '"').getBytes("UTF-8"));
        con.getInputStream();

    }

    /**
     * Deletes github repositories under a specific organization
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private void removeRepos() throws IOException
    {

        for (int i = 1; i <= this.numOfRepositories; i++)
        {

            GHRepository repo = this.organization.getRepository("Repo" + i);
            repo.delete();
            System.out.println("Deleting repo...");

        }

        repositories.clear();

    }

    /**
     * Creates and populates the team with members and assigns the team a repository
     * @throws IOException
     */
    private void setUpTeams() throws IOException
    {

        for (int i = 1; i <= this.numOfRepositories; i++)
        {

            GHTeam team = this.organization.createTeam("Team" + i, GHOrganization.Permission.PULL);
            team.add(github.getUser("ncoop57"));
            team.add(repositories.get(i - 1));
            System.out.println("Setting up team...");

        }

    }

    /**
     * Deletes teams
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private void removeTeams() throws IOException
    {

        for (int i = 1; i <= this.numOfRepositories; i++)
        {

            GHTeam team = this.organization.getTeamByName("Team" + i);
            team.delete();
            System.out.println("Deleting team...");

        }

    }

}
