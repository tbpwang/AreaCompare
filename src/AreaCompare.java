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

    private int baseTriangles;//baseTriangles:{3, 4, 5}.

    public AreaCompare(int limitNumber) {
        this.limitNumber = limitNumber;
    }

    public static void main(String[] args) {
        //
        double A1 = 0;
        double A2 = 0;
        double B1 = 0;
        double B2 = 0;

        AreaCompare area = new AreaCompare(100000);
        area.setBaseTriangles(4);
        double latitude;
        System.out.println("The arc " + area.getLimitNumber() + " subdivision :");
        System.out.println("The earth radius used here is " + RE + "km:");
        System.out.println("***************");
        double longitude = 2 * Math.PI / area.getBaseTriangles();
        //while (longitude > (Math.PI / 180)) {
        // System.out.println("The top angle is " + (longitude * 180 / Math.PI) + "°:");
        for (int i = 0; i < 19; i++) {
//            latitude =10 * i * Math.PI / 180;
            latitude = 5 * i * Math.PI / 180;
            System.out.println("The latitude is " + (latitude * 180 / Math.PI) + "°:");

            // the universal case is arcAB = Δφ/N * cos(latitude)
            //Δφ depends on arc AB ,Or ∠APB
            //It loops to get all significant values;
            double arcAB = Math.PI * RE * Math.cos(latitude) / (2 * area.getLimitNumber());
//          double arcAB = longitude / area.getLimitNumber() * RE * Math.cos(latitude);
            double arcPA = RE * (Math.PI / 2 - latitude);
            double arcPB = RE * (Math.PI / 2 - latitude);//,=arcPA from North pole to a latitude ;

            area.setAngles(new ArrayList<>());
            double sphereArea = area.calculateSphereArea(arcAB, arcPA, arcPB);

//          System.out.println("In latitude of π/" + (i/18) + ":");
            System.out.println(area.toString());
            System.out.println("Sphere triangle area is:" + (sphereArea * area.getLimitNumber()));

//            double sphericalCrown = Math.PI * Math.pow(RE, 2.0) * (2 - Math.sin(2 * latitude)) / (4 * area.getLimitNumber());
            double sphericalCrown = 2 * Math.PI * Math.pow(RE, 2.0) * (1 - Math.sin(latitude)) / (4 * area.getLimitNumber());
            System.out.println("Spherical Crown area is:" + sphericalCrown * area.getLimitNumber());

            if (Double.compare(latitude, 0.0) == 0) {
                A1 = sphericalCrown;
                B1 = sphereArea;
            }
            if (Double.compare(latitude, Math.PI / 6) == 0) {
                A2 = sphericalCrown;
                B2 = sphereArea;
            }

            double ratio = sphericalCrown / sphereArea;
            System.out.println("The ratio of sphericalCrown to sphereArea (sphericalCrown / sphereArea) is: " + ratio);

            double difference = area.getLimitNumber() * (sphericalCrown - sphereArea);
            System.out.println("The difference between sphericalCrown and sphereArea(sphericalCrown - sphereArea) is: " + difference);
            System.out.println("----------------");
        }
        //longitude = longitude / 2;
        System.out.println("A12 - B12 = " + ((A1 - A2) - (B1 - B2)) * area.getLimitNumber());
        System.out.println("A2 - B2 = " + (A2 - B2) * area.getLimitNumber());
        System.out.println("A1 - B1 = " + (A1 - B1) * area.getLimitNumber());

        System.out.println("##############################");
        //}
    }

    @Override
    public String toString() {
        String anglesToString = "Three angles are: ";
        for (int i = 0; i < getAngles().size(); i++) {
            anglesToString += (180 * getAngles().get(i) / Math.PI) + "°; ";
        }
        return anglesToString;
    }

    public int getBaseTriangles() {
        return baseTriangles;
    }

    public void setBaseTriangles(int baseTriangles) {
        this.baseTriangles = baseTriangles;
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
        double angleA;//equals to Δφ (depends on arc AB ,Or ∠APB)
        double angleB;
        double angleC;

//        angleA = Math.PI / 2 / getLimitNumber(); //here Δφ is substituted by PI/2
        angleA = Math.acos((Math.cos(edgeA / RE) - Math.cos(edgeB / RE) * Math.cos(edgeC / RE)) / (Math.sin(edgeB / RE) * Math.sin(edgeC / RE)));
        angleB = Math.acos((Math.cos(edgeB / RE) - Math.cos(edgeA / RE) * Math.cos(edgeC / RE)) / (Math.sin(edgeA / RE) * Math.sin(edgeC / RE)));
        angleC = Math.acos((Math.cos(edgeC / RE) - Math.cos(edgeB / RE) * Math.cos(edgeA / RE)) / (Math.sin(edgeB / RE) * Math.sin(edgeA / RE)));

        angles.add(angleA);
        angles.add(angleB);
        angles.add(angleC);

        return Math.pow(RE, 2.0) * (angleA + angleB + angleC - Math.PI);
    }


}
