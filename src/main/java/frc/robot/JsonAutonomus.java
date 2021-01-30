package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Timer;

public class JsonAutonomus extends Autonomous{
    private JsonElement auto;
	private List<AutoInstruction> instructions;
	private int step;
	private Timer timer;
	private double start;
	private double navxStart;
	private AHRS gyro;
	private Drivetrain drive;
	private double turnSpeed;
	private boolean red;
	private boolean edge;
	private final double TICKS_PER_ROTATION = 1; //tbd
	private final double TICKS_PER_INCH = TICKS_PER_ROTATION / (4 * Math.PI);
	private final double TICKS_PER_DEGREE = TICKS_PER_INCH * 1; //tbd (INCHES_PER_DEGREE)
	
	private FileReader fr;
	private JsonReader jr;
	private JsonParser jp;
		
	private enum Unit { Seconds, Milliseconds, EncoderTicks, Rotations, Inches, Feet, Degrees, Invalid };
	
	private static class AutoInstruction
	{
		public String type;
		public Unit unit;
		public double amount;
		public List<Double> args;
		public AutoInstruction(String type, Unit unit, double amount, List<Double> args)
		{
			this.type = type;
			this.unit = unit;
			this.amount = amount;
			this.args = args;
		}
    }
    
    /**
	 * Creates a JsonAutonomous from the specified file
	 * @param file The location of the file to parse
	 */
	public JsonAutonomus(String file, AHRS gyro, Drivetrain drive) {
		this.drive = drive;
		this.gyro = gyro;
		//todo: Add PID controls
		parseFile(file);
    }
    
    public void parseFile(String file)
	{		
		step = -1;
		timer = new Timer();
		instructions = new ArrayList<AutoInstruction>();
		try
		{
			fr = new FileReader(new File(file));
			jr = new JsonReader(fr);
			jp = new JsonParser();
			auto = jp.parse(jr);
			JsonElement inner = auto.getAsJsonObject().get("auto");
			if(inner.isJsonArray())
			{
				for(JsonElement e : inner.getAsJsonArray())
				{
					JsonObject o = e.getAsJsonObject();
					List<Double> tmp = new ArrayList<Double>();
					for(JsonElement e2 : o.get("args").getAsJsonArray())
					{
						tmp.add(e2.getAsDouble());
					}
					instructions.add(new AutoInstruction(o.get("type").getAsString(), parseUnit(o.get("unit").getAsString()), o.get("amount").getAsDouble(), tmp));
				}
			}
		}
		catch (JsonIOException | JsonSyntaxException | FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

    public static Unit parseUnit(String in)
	{
		return Unit.valueOf(in);
	}

	@Override
	public void run(){
		if(step == -1)
		{
			reset();
		}
		if(instructions.size() == step)
		{
			drive.arcadeDrive(0, 0);
			return;
		}
		AutoInstruction ai = instructions.get(step);
		if(ai.type.equals("drive"))
		{
			drive(ai);
		} else if (ai.type.equals("turnDeg")) {
			turnDegrees(ai);
		} else {
			System.out.println("Invalid Command");
			reset();
		}
	}

	/**
	 * @param s Speed
	 * @param t Time
	 * @return Done
	 */
	private boolean driveTime(double s, double t)
	{
		if(timer.get() < t)
		{
			drive.arcadeDrive(0, s);
		}
		else
		{
			return true;
		}
		return false;
	}
	
	/**
	 * @param s Speed
	 * @param d Distance
	 * @return Done
	 */
	private boolean driveDistance(double s, double d)
	{
		if(Math.abs(drive.getLeftEncoder()-start) < d)
		{
			drive.arcadeDrive(0, s);
		}
		else
		{
			return true;
		}
		return false;
	}

	/**
	 * @param speed Turn Speed
	 * @param deg Degrees turnt
	 * @return Done
	 */
	private boolean rotateDegrees(double speed, double deg )
	{
		if(Math.abs(getAngle()-navxStart-deg) < 0.5) { return true; }
		drive.arcadeDrive(deg > 0 ? speed : -speed, 0);
		return false;
	}

	private double getAngle(){
		double angle = gyro.getAngle() % 360;
		return (angle > 0) ? angle : angle + 360;
	}

	public void turnDegrees(AutoInstruction ai)
	{
		if(Math.abs(drive.getLeftEncoder()-start) < ai.amount * TICKS_PER_DEGREE)
		{
			rotateDegrees(ai.args.get(0), ai.amount);
		}
		else
		{
			drive.arcadeDrive(0, 0);
			reset();
		}
	}

	public void drive(AutoInstruction ai)
	{
		System.out.println(ai.args.get(2));
		Unit u = ai.unit;
		if(u.equals(Unit.Seconds) || u.equals(Unit.Milliseconds))
		{
			if(driveTime(ai.args.get(0), (u.equals(Unit.Seconds) ? ai.amount : ai.amount/1000.0)))
			{
				reset();
			}
		}
		else if(u.equals(Unit.EncoderTicks) || u.equals(Unit.Rotations))
		{
			if(driveDistance(ai.args.get(0), (u.equals(Unit.EncoderTicks) ? ai.amount : ai.amount*TICKS_PER_ROTATION)))
			{
				reset();
			}
		}
		else if(u.equals(Unit.Feet) || u.equals(Unit.Inches))
		{	
			if(driveDistance(ai.args.get(0), (u.equals(Unit.Inches) ? ai.amount*TICKS_PER_INCH : (ai.amount*TICKS_PER_INCH))*12.0))
			{
				reset();
			}
		}
	}

	private void reset()
	{
		step++;
		drive.arcadeDrive(0, 0);
		timer.reset();
		timer.start();
		start = drive.getLeftEncoder();
		navxStart =  getAngle();
		edge = true;
	}
}