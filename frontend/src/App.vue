<template>
  <v-app>
    <v-main>
      <v-container fluid class="pa-16">
        <h1>Jumia Exercise</h1>
        <v-card class="mt-10" dark>
          <v-card-title class="mb-3">
            <h3 class="mx-2">Phone numbers</h3>
            <v-spacer></v-spacer>
            <v-select v-model="filter.country"
                      class="mx-10"
                      style="max-width: 15vw"
                      :items="select.countries"
                      menu-props="auto"
                      label="Country"
                      hide-details
                      prepend-icon="mdi-map"
                      single-line
                      clearable/>
            <v-select v-model="filter.state"
                      style="max-width: 10vw"
                      :items="select.states"
                      menu-props="auto"
                      label="State"
                      hide-details
                      prepend-icon="mdi-flag"
                      single-line
                      clearable/>
          </v-card-title>
          <v-data-table
              :headers="headers"
              :items="customers"
              :options.sync="options"
              :server-items-length="total"
              :loading="loading"
              class="elevation-0">
            <template v-slot:item.country="{ item }">
              {{ constant.countries[item.country] }}
            </template>
            <template v-slot:item.countryCode="{ item }">
              {{ '+' + item.countryCode }}
            </template>
            <template v-slot:item.state="{ item }">
              <v-icon class="px-1" :color="item.state === 'VALID' ? 'green' : 'red'" x-small>mdi-circle</v-icon>
              {{ constant.states[item.state] }}
            </template>
          </v-data-table>
        </v-card>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import {countries, states} from "./constants";

export default {
  data() {
    return {
      total: 0,
      customers: [],
      loading: true,
      options: {},
      headers: [
        {text: 'Country', value: 'country'},
        {text: 'State', value: 'state'},
        {text: 'Country Code', value: 'countryCode'},
        {text: 'Number', value: 'number'}
      ],
      filter: {
        country: null,
        state: null
      },
      select: {
        countries: ['1', '2', '3'],
        states: [true, false]
      },
      constant: {
        countries: [],
        states: []
      }
    }
  },
  watch: {
    options: {
      handler() {
        this.getPhones()
      },
      deep: true,
    },
    filter: {
      handler() {
        this.getPhones()
      },
      deep: true,
    },
  },
  methods: {
    getPhones() {
      this.loading = true;

      const {page, itemsPerPage} = this.options

      this.axios.get('/phones', {
        params: {
          ...this.filter,
          page: page - 1,
          size: itemsPerPage
        }
      }).then((response) => {
        this.customers = response.data.content;
        this.total = response.data.totalElements
        this.loading = false
      });
    },
  },
  created() {
    this.constant.countries = countries.attributes;
    this.constant.states = states.attributes;
    this.select.countries = countries.options;
    this.select.states = states.options;
  }
}
</script>
