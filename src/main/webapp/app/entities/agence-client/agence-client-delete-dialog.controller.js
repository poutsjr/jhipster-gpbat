(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('AgenceClientDeleteController',AgenceClientDeleteController);

    AgenceClientDeleteController.$inject = ['$uibModalInstance', 'entity', 'AgenceClient'];

    function AgenceClientDeleteController($uibModalInstance, entity, AgenceClient) {
        var vm = this;

        vm.agenceClient = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AgenceClient.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
