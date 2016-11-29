(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('AvancementChantierDeleteController',AvancementChantierDeleteController);

    AvancementChantierDeleteController.$inject = ['$uibModalInstance', 'entity', 'AvancementChantier'];

    function AvancementChantierDeleteController($uibModalInstance, entity, AvancementChantier) {
        var vm = this;

        vm.avancementChantier = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AvancementChantier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
