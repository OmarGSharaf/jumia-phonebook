const path = require('path');

module.exports = {
    configureWebpack: {
        devtool: 'source-map',
        resolve: {
            extensions: ['*', '.js', '.vue', '.json']
        },
        module: {
            rules: [
                {
                    exclude: path.resolve(__dirname, 'node_modules')
                }
            ]
        }
    }
};
