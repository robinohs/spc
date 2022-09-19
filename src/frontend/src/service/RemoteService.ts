import CalculateByOrbitalParamsDTO from "../types/CalculateByOrbitalParamsDTO";
import Sat from "../types/Sat";
import SatPosition from "../types/SatPosition";

export const loadSatellitePositions = async (
  sats: Sat[],
  timeRange: [number, number]
): Promise<
  {
    name: string;
    positions: SatPosition[];
  }[]
> => {
  const promises = sats.map(async (sat) => {
    const dto: CalculateByOrbitalParamsDTO = {
      interval: {
        start: timeRange[0],
        end: timeRange[1],
      },
      satOrbitParams: sat.satOrbitParams,
    };
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dto),
    };
    const response = await fetch(
      "/api/position/by_orbital_params",
      requestOptions
    );
    const positions: SatPosition[] = await response.json();
    return {
      name: sat.name,
      positions,
    };
  });
  return Promise.all(promises);
};