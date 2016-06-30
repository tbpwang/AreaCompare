import java.util.ArrayList;
import java.util.List;

/**
 * Administrator
 * Created by tbpwang
 * 2016/6/28.
 */
public class AreaCompare {
    private static final double RE = 6371.393;//km,average numeric
    private int limitNumber;// = 10000000;//constant for subdivision latitude
    private List<Double> angles;

    public AreaCompare(int limitNumber) {
        this.limitNumber = limitNumber;
    }

    public static void main(String[] args) {
        AreaCompare area = new AreaCompare(100000);
        double latitude;
        System.out.println("The arc " + area.getLimitNumber() + " subdivision :");
        System.out.println("The earth radius used here is " + RE + "km:");
        System.out.println("********************");
        for (int i = 0; i < 19; i++) {
//            latitude =10 * i * Math.PI / 180;
            latitude = 5 * i * Math.PI / 180 ;

//            if (i==4) {
//                latitude = Math.PI/4;
//            }

            double arcAB = RE * Math.PI * Math.cos(latitude) / (2 * area.getLimitNumber());
            double arcPA = RE * (Math.PI / 2 - latitude);
            double arcPB = arcPA;//,from North pole to a latitude ;

            area.setAngles(new ArrayList<>());
            double sphereArea = area.calculateSphereArea(arcAB, arcPA, arcPB);
//            System.out.println("In latitude of π/" + (i/18) + ":");
            System.out.println("In latitude of " + (latitude * 180 / Math.PI) + "°:");
            System.out.println(area.toString());
            System.out.println("Sphere triangle area is:" + sphereArea);

//            double sphericalCrown = Math.PI * Math.pow(RE, 2.0) * (2 - Math.sin(2 * latitude)) / (4 * area.getLimitNumber());
            double sphericalCrown = 2 * Math.PI * Math.pow(RE, 2.0) * (1 - Math.sin(latitude)) / (4 * area.getLimitNumber());
            System.out.println("Spherical Crown area is:" + sphericalCrown);
            double rate = sphereArea / sphericalCrown;
            System.out.println("The rate of sphereArea to sphericalCrown is: " + rate);

            double difference = area.getLimitNumber()*(sphericalCrown - sphereArea);
            System.out.println("The difference between sphericalCrown and sphereArea is: " + difference);
            System.out.println("----------------");
        }
    }

    @Override
    public String toString() {
        String anglesToString = "Three angles are: ";
        for (int i = 0; i < angles.size(); i++) {
            anglesToString += (180 * angles.get(i) / Math.PI ) + "°; ";
        }
        return anglesToString;
    }

    public int getLimitNumber() {
        return limitNumber;
    }

    public List<Double> getAngles() {
        return angles;
    }

    public void setAngles(List<Double> angles) {
        this.angles = angles;
    }

    public double calculateSphereArea(double edgeA, double edgeB, double edgeC) {
        double angleA;
        double angleB;
        double angleC;

        angleA = Math.acos((Math.cos(edgeA / RE) - Math.cos(edgeB / RE) * Math.cos(edgeC / RE)) / (Math.sin(edgeB / RE) * Math.sin(edgeC / RE)));
        angleB = Math.acos((Math.cos(edgeB / RE) - Math.cos(edgeA / RE) * Math.cos(edgeC / RE)) / (Math.sin(edgeA / RE) * Math.sin(edgeC / RE)));
        angleC = Math.acos((Math.cos(edgeC / RE) - Math.cos(edgeB / RE) * Math.cos(edgeA / RE)) / (Math.sin(edgeB / RE) * Math.sin(edgeA / RE)));

        angles.add(angleA);
        angles.add(angleB);
        angles.add(angleC);

        return Math.pow(RE, 2.0) * (angleA + angleB + angleC - Math.PI);
    }


}
