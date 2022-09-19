import SatOrbitParams from "./SatOrbitParams";
import TimeInterval from "./TimeInterval";

interface CalculateByOrbitalParamsDTO {
  interval: TimeInterval;
  satOrbitParams: SatOrbitParams;
}

export default CalculateByOrbitalParamsDTO;
