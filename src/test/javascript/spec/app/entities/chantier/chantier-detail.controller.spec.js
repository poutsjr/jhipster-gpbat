'use strict';

describe('Controller Tests', function() {

    describe('Chantier Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockChantier, MockReceptionChantier, MockAvancementChantier, MockReserveChantier, MockClient, MockDiagnosticChantier, MockUtilisateur, MockCommande;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockChantier = jasmine.createSpy('MockChantier');
            MockReceptionChantier = jasmine.createSpy('MockReceptionChantier');
            MockAvancementChantier = jasmine.createSpy('MockAvancementChantier');
            MockReserveChantier = jasmine.createSpy('MockReserveChantier');
            MockClient = jasmine.createSpy('MockClient');
            MockDiagnosticChantier = jasmine.createSpy('MockDiagnosticChantier');
            MockUtilisateur = jasmine.createSpy('MockUtilisateur');
            MockCommande = jasmine.createSpy('MockCommande');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Chantier': MockChantier,
                'ReceptionChantier': MockReceptionChantier,
                'AvancementChantier': MockAvancementChantier,
                'ReserveChantier': MockReserveChantier,
                'Client': MockClient,
                'DiagnosticChantier': MockDiagnosticChantier,
                'Utilisateur': MockUtilisateur,
                'Commande': MockCommande
            };
            createController = function() {
                $injector.get('$controller')("ChantierDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gpbatApp:chantierUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
