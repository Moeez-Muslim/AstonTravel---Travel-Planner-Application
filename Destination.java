package astonTravel;

// A data structure to store a destination
public class Destination 
{

    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private String cost;

    public Destination(String name, String description, double latitude, double longitude, String cost) 
    {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cost = cost;
    }


    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public double getLatitude() 
    {
        return latitude;
    }

    public void setLatitude(double latitude) 
    {
        this.latitude = latitude;
    }

    public double getLongitude() 
    {
        return longitude;
    }

    public void setLongitude(double longitude) 
    {
        this.longitude = longitude;
    }

    public String getCost() 
    {
        return cost;
    }

    public void setCost(String cost) 
    {
        this.cost = cost;
    }

    @Override
    public String toString() 
    {
        return "Destination{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", cost='" + cost + '\'' +
                '}';
    }
}
