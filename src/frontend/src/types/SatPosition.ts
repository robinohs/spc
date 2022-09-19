import LLAPos from "./LLAPos";
import XYZPos from "./XYZPos";

interface SatPosition {
  epochMillis: number;
  llaPos: LLAPos;
  xyzPos: XYZPos;
}

export default SatPosition;
