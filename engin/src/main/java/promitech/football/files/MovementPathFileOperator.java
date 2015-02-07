package promitech.football.files;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import promitech.football.engine.MovementPath;
import promitech.football.engine.PlayfieldPoint;




public class MovementPathFileOperator {

	public static final Pattern pointPattern = Pattern.compile("\\((.*?),(.*?)\\)");
	
	public MovementPathFileOperator() {
	}
	
	public MovementPath read(String classPathFile) {
		MovementPath movementPath = null;
		try {
			InputStream is = MovementPathFileOperator.class.getResourceAsStream(classPathFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			String st = null;
			while ((st = br.readLine()) != null) {
				if (st.startsWith("#")) {
					continue;
				}
				MovementPath mp = createMovementPath(st, movementPath);
				if (mp == null) {
					throw new IllegalStateException("can not parse line " + st);
				}
				movementPath = mp;
			}
			br.close();
			return movementPath;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public MovementPath createMovementPath(String st) {
		return createMovementPath(st, null);
	}
	
	public MovementPath createMovementPath(String st, MovementPath movementPath) {
		MovementPath mp = null;
		Matcher matcher = pointPattern.matcher(st);
		while (matcher.find()) {
			PlayfieldPoint p = new PlayfieldPoint(
					Integer.parseInt(matcher.group(1)),
					Integer.parseInt(matcher.group(2))
					);
			if (mp == null) {
				mp = MovementPath.instanceWithReferenceToPath(movementPath, p);
			} else {
				mp.add(p);
			}
		}
		return mp;
	}
	
}
