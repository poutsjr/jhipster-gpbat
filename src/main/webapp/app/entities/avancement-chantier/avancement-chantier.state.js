(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('avancement-chantier', {
            parent: 'entity',
            url: '/avancement-chantier?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.avancementChantier.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/avancement-chantier/avancement-chantiers.html',
                    controller: 'AvancementChantierController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('avancementChantier');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('avancement-chantier-detail', {
            parent: 'entity',
            url: '/avancement-chantier/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.avancementChantier.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/avancement-chantier/avancement-chantier-detail.html',
                    controller: 'AvancementChantierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('avancementChantier');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AvancementChantier', function($stateParams, AvancementChantier) {
                    return AvancementChantier.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'avancement-chantier',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('avancement-chantier-detail.edit', {
            parent: 'avancement-chantier-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/avancement-chantier/avancement-chantier-dialog.html',
                    controller: 'AvancementChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AvancementChantier', function(AvancementChantier) {
                            return AvancementChantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('avancement-chantier.new', {
            parent: 'avancement-chantier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/avancement-chantier/avancement-chantier-dialog.html',
                    controller: 'AvancementChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                avancementPourcentage: null,
                                avancementEtat: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('avancement-chantier', null, { reload: 'avancement-chantier' });
                }, function() {
                    $state.go('avancement-chantier');
                });
            }]
        })
        .state('avancement-chantier.edit', {
            parent: 'avancement-chantier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/avancement-chantier/avancement-chantier-dialog.html',
                    controller: 'AvancementChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AvancementChantier', function(AvancementChantier) {
                            return AvancementChantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('avancement-chantier', null, { reload: 'avancement-chantier' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('avancement-chantier.delete', {
            parent: 'avancement-chantier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/avancement-chantier/avancement-chantier-delete-dialog.html',
                    controller: 'AvancementChantierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AvancementChantier', function(AvancementChantier) {
                            return AvancementChantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('avancement-chantier', null, { reload: 'avancement-chantier' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
