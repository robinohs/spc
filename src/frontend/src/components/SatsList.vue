<template>
    <div class="sats-list">
        <h2>Satellites</h2>
        <table class="table table-sm">
            <thead class="thead-dark">
                <tr>
                    <th scope="col">Name</th>
                    <th scope="col">Epoch</th>
                    <th scope="col">Mean anomaly [deg]</th>
                    <th scope="col">Alt. [km]</th>
                    <th scope="col">Perigee arg. [deg]</th>
                    <th scope="col">Incl. [deg]</th>
                    <th scope="col">Raan [deg]</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="sat in sats" :key="sat.name">
                    <th scope="row">{{sat.name}}</th>
                    <td>{{new Date(sat.satOrbitParams.epoch).toISOString()}}</td>
                    <td>{{sat.satOrbitParams.meanAnomaly}}</td>
                    <td>{{sat.satOrbitParams.altitude}}</td>
                    <td>{{sat.satOrbitParams.perigeeArgument}}</td>
                    <td>{{sat.satOrbitParams.inclination}}</td>
                    <td>{{sat.satOrbitParams.raan}}</td>
                </tr>
                <tr v-if="sats.length === 0">
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>-</td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from "vue";
import Sat from "../types/Sat";

export default defineComponent({
    props: {
        sats: {
            required: true,
            type: Array as PropType<Sat[]>
        }
    }
})
</script>

<style>

</style>