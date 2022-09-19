<template>
  <div class="app">
    <div class="container py-3 min-vh-100">
      <Header />
      <div class="mb-4">
        <input type="file" accept=".csv" @change="handleFileChange($event)" />
        <button type="button" class="btn btn-primary me-3" @click="handleUpload"
          :disabled="isDisabledUpload">Upload</button>
        <button type="button" class="btn btn-primary me-3" @click="handleCalculateClick"
          :disabled="isDisabledCalculation">Calculate</button>
      </div>
      <CalculationSettings :time-range="timeRange" v-on:update="handleTimeRangeUpdate" />
      <SatsList :sats="sats" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { saveAs } from "file-saver";
import JSZip from "jszip";
import { DateTime } from "luxon";
import moment from "moment";
import Papa, { ParseResult } from 'papaparse';
import { computed, ref } from "vue";
import CalculationSettings from "./components/CalculationSettings.vue";
import Header from "./components/Header.vue";
import SatsList from "./components/SatsList.vue";
import { loadSatellitePositions } from "./service/RemoteService";
import Sat from "./types/Sat";

const timeRange = ref<[Date, Date] | null>(null);
const timeRangeMillis = ref<[number, number] | null>(null);
const file = ref<File | null>(null);
const isDisabledUpload = computed(() => file.value === null)
const isDisabledCalculation = computed(() => timeRange.value === null || sats.value.length === 0)

const handleTimeRangeUpdate = (newTimeRange: [number, number]) => {
  console.log(newTimeRange);
  const startDate = DateTime.fromMillis(newTimeRange[0], { zone: "utc" }).setZone("local", { keepLocalTime: true }).toJSDate()
  const endDate = DateTime.fromMillis(newTimeRange[1], { zone: "utc" }).setZone("local", { keepLocalTime: true }).toJSDate()
  console.log([startDate, endDate]);
  timeRange.value = [startDate, endDate];
  timeRangeMillis.value = newTimeRange
}
const handleCalculateClick = async () => {
  if (timeRangeMillis.value === null || sats.value.length === 0) return;
  const remoteData = await loadSatellitePositions(sats.value, timeRangeMillis.value)
  const xyzPositions = remoteData.map((data) => {
    const name = `${data.name}-XYZ-Pos.csv`;
    const xyzPos = data.positions.map((position) => ({
      "TIME[UTC]": DateTime.fromMillis(position.epochMillis, { zone: "utc" }).toFormat("d MMM yyyy HH:mm:ss.SSS"),
      "X[km]": position.xyzPos.x,
      "Y[km]": position.xyzPos.y,
      "Z[km]": position.xyzPos.z,
    }));
    const csv = Papa.unparse(xyzPos, {
      header: true
    })
    return {
      name,
      content: csv
    }
  })
  const llaPositions = remoteData.map((data) => {
    const name = `${data.name}-LLA-Pos.csv`;
    const llaPos = data.positions.map((position) => ({
      "TIME[UTC]": DateTime.fromMillis(position.epochMillis, { zone: "utc" }).toFormat("d MMM yyyy HH:mm:ss.SSS"),
      "LAT[deg]": position.llaPos.lat,
      "LON[deg]": position.llaPos.lon,
      "ALT[km]": position.llaPos.alt,
    }));
    const csv = Papa.unparse(llaPos, {
      header: true
    })
    return {
      name,
      content: csv
    }
  });
  const zip = new JSZip();
  xyzPositions.forEach((xyzPos) => {
    zip.file(xyzPos.name, xyzPos.content)
  });
  llaPositions.forEach((llaPos) => {
    zip.file(llaPos.name, llaPos.content)
  });
  const blob = await zip.generateAsync({ type: "blob" });
  saveAs(blob, "positions.zip");
}
const handleFileChange = (event: Event) => {
  if (!event.target) return
  const target = event.target as HTMLInputElement;
  if (target.files === null || target.files[0] === null) return
  file.value = target.files[0]
}
const handleUpload = () => {
  if (file.value === null) return
  Papa.parse(file.value, {
    header: true,
    dynamicTyping: true,
    complete: (results: ParseResult<Record<string, unknown>>) => {
      const newSats: Sat[] = []
      results.data.forEach((row) => {
        const satName = row["SAT_NAME"] as string;
        const epoch = moment.utc(row["EPOCH"] as string, "D MMM YYYY HH:mm:ss.SSS")
        const meanAnomaly = row["MEAN_ANOMALY[deg]"] as number;
        const altitude = row["ALTITUDE[km]"] as number;
        const perigeeArgument = row["PERIGEE_ARGUMENT[deg]"] as number;
        const inclination = row["INCLINATION[deg]"] as number;
        const raan = row["RAAN[deg]"] as number;
        newSats.push({
          name: satName,
          satOrbitParams: {
            epoch: epoch.valueOf(),
            meanAnomaly,
            altitude,
            perigeeArgument,
            inclination,
            raan
          }
        })
      })
      sats.value = newSats
    }
  });
}

const sats = ref<Sat[]>([])

</script>

<style>

</style>