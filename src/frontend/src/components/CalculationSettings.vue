<template>
    <div class="card mb-3">
        <div class="card-body">
            <div class="mb-3">
                <h5>Time range [UTC]</h5>
                <Datepicker :model-value="timeRange" @update:modelValue="setDate" format="dd.MM.yyyy HH:mm:ss"
                    :range="true" :utc="false" :enableSeconds="true" />
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';
import { DateTime } from 'luxon';

const { timeRange } = defineProps<{
    timeRange: [Date, Date] | null
}>()

const emit = defineEmits<{
    (e: 'update', timeRange: [number, number]): void
}>()

const setDate = (update: [Date, Date]) => {
    const start = DateTime.fromJSDate(update[0]).setZone("utc", { keepLocalTime: true }).toMillis();
    const end = DateTime.fromJSDate(update[1]).setZone("utc", { keepLocalTime: true }).toMillis();
    emit("update", [start, end]);
}


</script>